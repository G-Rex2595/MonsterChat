package wifinderinc.wifinder;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.LinkedList;

import wifinderinc.wifinder.Message;

/**
  * @author Michael Young
  */
public class P2PManager {
	//Fields
	private String roomName;                            //Name of the room
	private String passwd;                              //Password for the room
	private LinkedList<Message> incomingMessageQueue;   //Incoming messages to send to room
	private LinkedList<Message> outgoingMessageQueue;   //Outgoing messages
	private WifiP2pManager p2pManager;                  //The Wifip2pmanager system service
	private WifiP2pManager.Channel channel;             //The Wifi p2p channel
    private WifiBroadcastReceiver receiver;             //The receiver listening for intents from other devices
    private Activity activity;                          //Activity this p2p manager is associated with
    private IntentFilter intentFilter;                  //Filter for the intents the broadcast receiver will take

	//Singleton Object
	private static P2PManager instance = null;

	/**
	 * Constructor for P2PManager. Private to enforce a single instance.
	 */
	public P2PManager(Activity activity) {

		//Initialize components for Wifi p2p
        p2pManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = p2pManager.initialize(activity, activity.getMainLooper(), null);
        receiver = new WifiBroadcastReceiver(p2pManager, channel, this.activity);

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
            }

            @Override
            public void onFailure(int reason) {
                //TODO: tell user it failed
            }
        });

        //Create message queues
        incomingMessageQueue = new LinkedList<>();
        outgoingMessageQueue = new LinkedList<>();

        roomName = null;
        passwd = null;
        this.activity = activity;

    }
	
	/**
	  *
	  */
	public void sendMessage(Message msg) {
		synchronized (outgoingMessageQueue) {
			outgoingMessageQueue.add(msg);
		}
	}

	public void setPassword(String passwd) {
		this.passwd = passwd;
	}

	public void setRoom(String roomName) {
		this.roomName = roomName;
	}

    /**
     * Registers the receiver for Wifi P2P connections for the app. Does nothing fi the receiver
     * has not been created yet.
     */
    public void registerReceiver() {
        if (receiver == null) return;
        activity.registerReceiver(receiver, intentFilter);
    }

    /**
     * Unregister the broadcast receiver if the app is paused. Does nothing if the receiver has
     * not been created yet.
     */
    public void unregisterReceiver() {
        if (receiver == null) return;
        activity.unregisterReceiver(receiver);
    }

    private void connect() {

    }
}
