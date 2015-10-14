package wifinderinc.wifinder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
  * @author Michael Young
  */
public class P2PManager {
	//Fields
	private final HashSet<Integer> MESSAGE_HASHES;              //Incoming messages to send to room
	private WifiP2pManager p2pManager;                          //The Wifip2pmanager system service
	private WifiP2pManager.Channel channel;                     //The Wifi p2p channel
	private WifiBroadcastReceiver receiver;                     //The receiver listening for intents from other devices
	private Activity activity;                                  //Activity this p2p manager is associated with
	private IntentFilter intentFilter;                          //Filter for the intents the broadcast receiver will take
    private final ArrayList<ObjectOutputStream> OUTPUT_STREAMS; //List of socket outputs for writing messages
    private ChatRoom chatroom;                                  //Chat room the device is currently part of

    /**
     * Construct for P2PManager.
     * @param activity The activity the P2PManager is associated with.
     */
	public P2PManager(Activity activity) {
		//Initialize components for Wifi p2p
        p2pManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = p2pManager.initialize(activity, activity.getMainLooper(), null);
        receiver = new WifiBroadcastReceiver(p2pManager, channel, this);

        intentFilter = new IntentFilter();

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        p2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //Discovery service started, but nothing found. Ignore
                Log.d("P2PManager", "discoverPeers success");
            }

            @Override
            public void onFailure(int reason) {
                //TODO: tell user it failed
                Log.d("P2PManager", "discoverPeers failed - " + reason);
            }
        });

        //Create message queues
        MESSAGE_HASHES = new HashSet<>();
        OUTPUT_STREAMS = new ArrayList<>();

        chatroom = null;
        this.activity = activity;

    }

    /**
     * Sends a message to all devices connected to this device. This call will invoke a new thread
     * that will send the messages and then exit.
     * @param msg The message being sent.
     */
	public void sendMessage(final Message msg) {
        Log.d("P2PManager", "sendMessage " + msg.getMessage());
        synchronized (MESSAGE_HASHES) {
            MESSAGE_HASHES.add(msg.hashCode());
        }
		new Thread() {
            @Override
            public void run() {
                synchronized (OUTPUT_STREAMS) {
                    Log.d("OOS", "Stream count:  " + OUTPUT_STREAMS.size());
                    for (ObjectOutputStream oos : OUTPUT_STREAMS) {
                        try {
                            oos.writeObject(msg);
                            oos.flush();
                            Log.d("OOS", "We wrote");
                        } catch (IOException e) {
                            //TODO error handle
                            Log.d("OOS", "Failed to send");
                        }
                    }
                }
            }
        }.start();
	}

    /**
     * Receive a message from another device.
     * @param msg
     */
    public void receiveMessage(Message msg) {
        Log.d("P2PManager", "receieveMessage " + msg.toString());
        if (chatroom == null) return; //Not part of any room.
        synchronized (MESSAGE_HASHES) {
            if (MESSAGE_HASHES.contains(msg.hashCode())) return; //Already has got this message.
        }
        sendMessage(msg);           //Forward the message on to other devices. This will also add the hash.
        //chatroom.sendMessage(msg);  //Send it to the app
        ((ChatRoomView) (activity)).addMessage(msg);
    }

    public void setChatRoom(ChatRoom chatroom) {
        Log.d("P2PManager", "setChatRoom called");
        this.chatroom = chatroom;
        this.chatroom.sendMessage(new Message("SYSTEM", "P2PManager setChatRoom", null, "Global"));
    }

    /**
     * Registers the receiver for Wifi P2P connections for the app. Does nothing if the receiver
     * has not been created yet.
     */
    public void registerReceiver() {
        Log.d("P2PManager", "registerReceiver");
        if (receiver == null) return;
        activity.registerReceiver(receiver, intentFilter);
    }

    /**
     * Unregister the broadcast receiver if the app is paused. Does nothing if the receiver has
     * not been created yet.
     */
    public void unregisterReceiver() {
        Log.d("P2PManager", "unregisterReceiver");
        if (receiver == null) return;
        activity.unregisterReceiver(receiver);
    }

    /**
     * Close the P2PManager. This stops peer discovery, closes all streams associated with
     * the P2PManager, and stops threads spawned by the P2PManager
     */
    public void close() {
        p2pManager.stopPeerDiscovery(channel, null);
        clearConnections();
    }

    /**
     * Get a list of rooms that the device can join. To be finished on a later sprint.
     * @return
     */
    public LinkedList<String> getAvailableRooms() {
        return null;
    }

    /**
     * Adds a connection stream to the manager that will be sent messages.
     * @param oos The stream to be added to the P2PManager
     */
    public void addConnection(ObjectOutputStream oos) {
        synchronized (OUTPUT_STREAMS) {
            OUTPUT_STREAMS.add(oos);
        }
    }

    /**
     * Clears all the output streams associated with the P2PManager.
     */
    public void clearConnections() {
        synchronized (OUTPUT_STREAMS) {
            //First, we need to close all streams
            for (ObjectOutputStream oos : OUTPUT_STREAMS) {
                try {
                    oos.close();
                }
                catch (IOException e) {
                    //Abandon hope
                }
            }
            //Finally, clear the list for new ones
            OUTPUT_STREAMS.clear();
        }
    }
}
