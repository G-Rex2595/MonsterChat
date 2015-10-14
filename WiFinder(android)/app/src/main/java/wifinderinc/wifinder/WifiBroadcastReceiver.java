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
import android.util.Log;

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
        Log.d("WifiBroadcastReceiver", "constructor");
    }

    /**
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WifiBroadcastReceiver", "onReceiver");
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.d("WifiBroadCastReceiver", "WIFI_P2P_STATE_CHANGED_ACTION");
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
            Log.d("WifiBroadCastReceiver", "WIFI_P2P_PEERS_CHANGED_ACTION");
            if (manager != null) {
                manager.requestPeers(channel, peerListListener);
            }

        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //Connection state changed.
            Log.d("WifiBroadCastReceiver", "WIFI_P2P_CONNECTION_CHANGED_ACTION");
            if (manager == null) return;

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                //Connection with some device
                manager.requestConnectionInfo(channel, connectionListener);

            }
            else {
                //Disconnect
                Log.d("WifiBroadCastReceiver", "WIFI_P2P_CONNECTION_CHANGED_ACTION_Disconnection?");
            }
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d("WifiBroadCastReceiver", "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }

    private ArrayList<WifiP2pDevice> peers = new ArrayList();
    private ArrayList<String> successes = new ArrayList<>();
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            Log.d("peerListListener", "onPeersAvailable");
            //Clear the original list and add to the new list
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            for (WifiP2pDevice device : peers) {
                boolean exists = false;
                for (String s: successes)
                {
                    if (s.equals(device.deviceAddress)) {
                        exists = true;
                        break;
                    }
                }

                if (exists)
                    continue;
                else
                    successes.add(device.deviceAddress);

                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //The intent P2P_CONNECTION_CHANGED.. will notify us.
                        //This can be ignored.
                        Log.d("Manager.connect", "Successful connection");
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {

                        }
                    }

                    @Override
                    public void onFailure(int reason) {
                        //TODO error handle (maybe?)
                        Log.d("Manager.connect", "Successful failed " + reason);
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
            Log.d("ConnectionListener", "onConnectionInfoAvailable");
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                Log.d("ConnectionListener", "Start server thread");
                new ServerThread().start();

            }
            else if (info.groupFormed) {
                Log.d("ConnectionListener", "Start Client thread");
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(groupOwnerAddress, PORT);
                            //ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                            //ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                            //output.writeObject(new Message("ConnectionInfoListener", "LOL U DIED", null, "Global"));
                            //p2pmanager.addConnection(output);
                            boolean notAdded = true;
                            while (socket.isConnected()) {
                                if (notAdded)
                                {
                                    notAdded = false;
                                    p2pmanager.addConnection(new ObjectOutputStream(socket.getOutputStream()));
                                }
                                //Message m = (Message) read.readObject();
                                Message m = (Message)new ObjectInputStream(socket.getInputStream()).readObject();
                                Log.d("ListeningThread", "Received message " + m.getMessage());
                                p2pmanager.receiveMessage(m);
                            }
                            socket.close();
                            //We only need to close the read end. The P2PManager will close all write ends.
                            //read.close();
                        }
                        catch (IOException e) {

                        }
                        catch (ClassNotFoundException cnfe) {}
                    }
                };
                t.start();
            }
        }
    };

    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(PORT);
                while (!isInterrupted()) {
                    final Socket s = ss.accept();
                    Log.d("Server Thread", "Connection started, dispatching thread");
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Log.d("Server dispatch thread", "Running");
                                //ObjectInputStream read = new ObjectInputStream(s.getInputStream());
                                //ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
                                //output.writeObject(new Message("ServerThread", "LOL U DIED", null, "Global"));
                                //p2pmanager.addConnection(output);
                                Log.d("Server dispatch thread", "Added connection");
                                boolean notAdded = true;
                                while (s.isConnected()) {
                                    if (notAdded)
                                    {
                                        notAdded = false;
                                        p2pmanager.addConnection(new ObjectOutputStream(s.getOutputStream()));
                                    }
                                    Log.d("Server dispatch thread", "Blocking on readObject");
                                    //Message m = (Message) read.readObject();
                                    Message m = (Message)new ObjectInputStream(s.getInputStream()).readObject();
                                    Log.d("Server dispatch thread", "Received message " + m.getMessage());
                                    p2pmanager.receiveMessage(m);
                                }
                                Log.d("Server dispatch thread", "Socket closed");
                                s.close();
                                //We only need to close the read end. The P2PManager will close all write ends.
                                //read.close();
                            }
                            catch (IOException e) {

                            }
                            catch (ClassNotFoundException cnfe) {}
                        }
                    };
                    t.start();
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
            Log.d("Clientthread", "Constructor");
            try {
                socket = new Socket(address, PORT);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                //ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                //p2pmanager.addConnection(output);
                boolean notAdded = true;
                while (socket.isConnected()) {
                    if (notAdded)
                    {
                        notAdded = false;
                        p2pmanager.addConnection(new ObjectOutputStream(socket.getOutputStream()));
                    }
                    //Message m = (Message) read.readObject();
                    Message m = (Message)new ObjectInputStream(socket.getInputStream()).readObject();
                    p2pmanager.receiveMessage(m);
                }
                socket.close();
                //We only need to close the read end. The P2PManager will close all write ends.
                //read.close();

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
