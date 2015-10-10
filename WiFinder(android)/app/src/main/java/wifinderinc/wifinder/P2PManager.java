package wifinderinc.wifinder;

import android.app.Activity;
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
    private IntentFilter intentFilter;

	//Singleton Object
	private static P2PManager instance = null;

	/**
	 * Constructor for P2PManager. Private to enforce a single instance.
	 */
	private P2PManager() {
		roomName = null;
		passwd = null;
		incomingMessageQueue = new LinkedList<>();
		outgoingMessageQueue = new LinkedList<>();
        p2pManager = null;
        channel = null;
        receiver = null;

        intentFilter = new IntentFilter();

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

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
     * Gets the instance of the P2PManager.
     * @return The P2PManager instance
     */
	public static P2PManager getInstance() {
		if (instance == null) instance = new P2PManager();
		return instance;
	}

    /**
     *
     * @param p2pManager
     */
	public void addWifiP2PManager(WifiP2pManager p2pManager) {
		this.p2pManager = p2pManager;
        if (this.p2pManager != null && this.channel != null && activity != null) {
            createReceiver();
        }
	}

    /**
     *
     * @param channel
     */
	public void addChannel(WifiP2pManager.Channel channel) {
		this.channel = channel;
        if (this.p2pManager != null && this.channel != null && activity != null) {
            createReceiver();
        }
	}

    /**
     * Add the activity to the P2PManager. Needed for registering the broadcast receiver.
     * @param activity The activity being added for P2P Connection
     */
    public void addActivity(Activity activity) {
        this.activity = activity;
        if (this.p2pManager != null && this.channel != null && activity != null) {
            createReceiver();
        }
    }

    /**
     * Creates the broadcast receiver for p2p connections.
     */
    private void createReceiver() { receiver = new WifiBroadcastReceiver(p2pManager, channel, activity); }

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
}
