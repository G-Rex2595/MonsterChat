package wifinderinc.wifinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Michael on 10/10/2015.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {

    public static final int PORT = 6223;

    //Fields
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private P2PManager p2pmanager;

    public WifiBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, P2PManager p2pmanager) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.p2pmanager = p2pmanager;
    }

    /**
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //Wifi direct is enabled
            }
            else {
                //Wifi direct is disabled

            }
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //The list of peers has changed. Update the list of devices to connect to
            if (manager != null) {
                manager.requestPeers(channel, peerListListener);
            }


        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //Connection state changed.
            if (manager == null) return;

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                //Connection with some device
                manager.requestConnectionInfo(channel, connectionListener);
            }
            else {
                //Disconnect
            }
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }

    private ArrayList<WifiP2pDevice> peers = new ArrayList();
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            //Clear the original list and add to the new list
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            for (WifiP2pDevice device : peers) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //The intent P2P_CONNECTION_CHANGED.. will notify us.
                        //This can be ignored.
                    }

                    @Override
                    public void onFailure(int reason) {
                        //TODO error handle (maybe?)
                    }
                });
            }
        }
    };

    /**
     *
     */
    private ConnectionInfoListener connectionListener = new ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                new ServerThread().start();

            }
            else if (info.groupFormed) {
                new ClientThread(info.groupOwnerAddress).start();
            }
        }
    };

    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(PORT);
                while (!isInterrupted()) {
                    Socket s = ss.accept();
                    new ClientThread(s).start();    //We can reuse the client thread class because it will do the same things
                }
            }
            catch (IOException e) {
                //TODO error handle
            }
        }
    }

    private class ClientThread extends Thread {

        private Socket socket;

        public ClientThread(InetAddress address) {
            try {
                socket = new Socket(address, PORT);
            }
            catch (IOException e) {
                //TODO error handle
            }
        }

        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                p2pmanager.addConnection(output);
                while (socket.isConnected()) {
                    Message m = (Message) read.readObject();
                    p2pmanager.receiveMessage(m);
                }
                socket.close();
                //We only need to close the read end. The P2PManager will close all write ends.
                read.close();

            }
            catch (IOException e) {
                //TODO error handle
                //Most likely cause of this is connection dropped
            } catch (ClassNotFoundException e) {
                //TODO error handle
                //If this happens, we have some major problems to consider.
                //The first case I can see this happening is two different versions of the app
                //and the message object has changed between them.
            }
        }
    }
}