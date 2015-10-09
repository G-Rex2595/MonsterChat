package team14.wifinder;    //TODO:  Set package to the final one we use

import java.util.LinkedList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class ChatRoom
{
    /**
     * Contains a reference to the P2PManager
     */
    private P2PManager _manager;

    /**
     * Holds a string of the username.
     */
    private String _username;

    /**
     * Holds a string of the chat room name.
     */
    private String _roomName;

    /**
     * Contains a reference to the ChatView
     */
    private ChatView _view;

    /**
     * Holds a list of Messages.
     */
    private LinkedList<Message> _messages;

    /**
     * ChatRoom constructor initializes fields.
     *
     * @param manager   Contains a reference to the P2PManager
     * @param username  Holds a string of the username.
     * @param roomName  Holds a string of the chat room name.
     */
    public ChatRoom(P2PManager manager, String username, String roomName)
    {
        _manager = manager;
        _username = username;
        _roomName = roomName;
        _messages = new LinkedList<Message>();
        _view = null;
    }   //end of ChatRoom constructor

    /**
     * Returns a list of Messages.
     *
     * @return  Returns a list of Messages.
     */
    public LinkedList<Message> getMessages()
    {
        return _messages;
    }   //end of getMessages method

    /**
     * Adds the message received from the ChatView to the message list.
     * Calls P2PManager's sendMessage to send a message.
     *
     * @param message   Holds the message to be saved and sent.
     */
    public void sendMessage(Message message)
    {
        _messages.add(message);
        P2PManager.getInstance().sendMessage(message);
    }   //end of sendMessage method

    /**
     * Adds the message received from the P2PManager to the message list.
     * Calls ChatView's addMessage to add a message.
     *
     * @param message   Holds the message to be saved and added.
     */
    public void addMessage(Message message)
    {
        _messages.add(message);
        _view.addMessage(message);
    }   //end of addMessage method

    /**
     * Calls ChatView's failedConnect.
     */
    public void failedConnect()
    {
        _view.failedConnect();
    }   //end of failedConnect method

    /**
     * TODO:  Finish in a later sprint
     */
    public void close()
    {
        //call LogWriter's close
    }   //end of close method

    /**
     * Sets the view to the given ChatView.
     *
     * @param view  Contains a reference to the ChatView.
     */
    public void setChatView(ChatView view)
    {
        _view = view;
    }   //end of setChatView method

    /**
     * Sets the username to the given string.
     *
     * @param username  Holds a string of the username.
     */
    public void setUsername(String username)
    {
        _username = username;
    }   //end of setUsername method
}   //end of ChatRoom class
