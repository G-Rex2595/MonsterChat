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
	
	//Singleton object, no instantiation
	private P2PManager() { }
	
	/**
	  *
	  */
	public void sendMessage(Message msg) {
		
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
