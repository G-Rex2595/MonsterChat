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

import java.io.BufferedReader;
import java.io.FileReader;
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

    private static final String p2pInt = "p2p-p2p0";
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
                        catch (Exception e) {

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
                new ServerThread(p2pmanager, PORT).start();

            }
            else if (info.groupFormed) {
                Log.d("ConnectionListener", "Start Client thread");
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Socket socket = new Socket(groupOwnerAddress, PORT);
                                //ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                                //ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                                //output.writeObject(new Message("ConnectionInfoListener", "LOL U DIED", null, "Global"));
                                //p2pmanager.addConnection(output);
                                p2pmanager.addConnection(new ObjectOutputStream(socket.getOutputStream()));
                                ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
                                while (socket.isConnected()) {
                                    //Message m = (Message) read.readObject();
                                    Message m = (Message) read.readObject();
                                    Log.d("ListeningThread", "Received message " + m.getMessage());
                                    p2pmanager.receiveMessage(m);
                                }
                                Log.d("ConnectionInfoListener", "Left Socket Loop");
                                socket.close();
                                read.close();
                                break;
                                //We only need to close the read end. The P2PManager will close all write ends.
                                //read.close();
                            } catch (IOException e) {
                                Log.d("ConnectionInfoListener", "IOException");
                                e.printStackTrace();
                            } catch (ClassNotFoundException cnfe) {
                                Log.d("ConnectionInfoListener", "ClassNotFoundException");
                            }
                        }
                    }
                };
                t.start();
            }
        }
    };



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
                Log.d("Clientthread", "Left Socket Loop");
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


    /**
     * Get the IP of some device from the ARP cache.
     * We assume the ARP cache is in /proc/net/arp since Android is Linux based.
     * We also assume /proc/net/arp has the following format
     * IP address       HW type     Flags       HW address            Mask     Device
     * 192.168.18.11    0x1         0x2         00:04:20:06:55:1a     *        eth0
     * 192.168.18.36    0x1         0x2         00:22:43:ab:2a:5b     *        eth0
     * This is a modification of http://www.flattermann.net/2011/02/android-howto-find-the-hardware-mac-address-of-a-remote-host/
     * @param MAC The MAC address of the device being looked up
     * @return The IP of the device that was looked up, or null
     */
    private InetAddress getIPFromMac(String MAC) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {

                String[] split = line.split(" +");
                if (split != null && split.length >= 4) {
                    // Basic sanity check
                    String device = split[5];
                    if (device.matches(".*" +p2pInt+ ".*")){
                        String mac = split[3];
                        if (mac.matches(MAC)) {
                            return InetAddress.getByName(split[0]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}