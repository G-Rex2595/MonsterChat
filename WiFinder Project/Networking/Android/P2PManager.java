import java.util.LinkedList;

/**
  * @author Michael Young
  */
public class P2PManager {
	//Fields
	private String roomName;
	private String passwd;
	private LinkedList<Message> incomingMessageQueue;
	private LinkedList<Message> outgoingMessageQueue;
	
	/**
	  * Constructor for P2PManager
	  * @param roomName The name of the room the user belongs to.
	  * @param passwd The password of the room, if any.
	  */
	public P2PManager(String roomName, String passwd) {
		this.roomName = roomName;
		this.passwd = passwd;
		incomingMessageQueue = new LinkedList<>();
		outgoingMessageQueue = new LinkedList<>();
	}
	
	/**
	  *
	  */
	public void sendMessage(Message msg) {
		synchronized (outgoingMessageQueue) {
			outgoingMessageQueue.add(msg);
		}
	}
	
	/**
	  *
	  */
	public void setPassword(String passwd) {
		this.passwd = passwd;
	}
	
	/**
	  * 
	  */
	public void setRoom(String roomName) {
		this.roomName = roomName;
	}
	
}
