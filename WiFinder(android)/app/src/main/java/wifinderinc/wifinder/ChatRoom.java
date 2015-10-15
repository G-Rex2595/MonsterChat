package wifinderinc.wifinder;

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
    private String _chatRoomName;

    /**
     * Contains a reference to the ChatRoomView.
     */
    private ChatRoomView _view;

    /**
     * Holds a list of Messages.
     */
    private LinkedList<Message> _messages;

    /**
     * Holds a string of the id.
     */
    private String _id;

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
        _chatRoomName = roomName;
        _messages = new LinkedList<Message>();
        _id = null;
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
     * Adds the message received from the ChatRoomView to the message list.
     * Calls P2PManager's sendMessage to send a message.
     *
     * @param str   Holds the message to be saved and sent.
     */
    public void sendMessage(String str)
    {
        Message message = new Message(_username, str, _id, _chatRoomName);
        _messages.add(message);
        _view.addMessage(message);
        _manager.sendMessage(message);
    }   //end of sendMessage method

    /**
     * Adds the message received from the P2PManager to the message list.
     * Calls ChatRoomView's addMessage to add a message.
     *
     * @param message   Holds the message to be saved and added.
     */
    public void addMessage(Message message)
    {
        _messages.add(message);
        _view.addMessage(message);
    }   //end of addMessage method

    /**
     * Calls ChatRoomView's failedConnect.
     */
    public void failedConnect()
    {
        //_view.failedConnect();
    }   //end of failedConnect method

    /**
     * TODO:  Finish in a later sprint
     */
    public void close()
    {
        //call LogWriter's close
    }   //end of close method

    /**
     * Sets the view to the given ChatRoomView.
     *
     * @param view  Contains a reference to the ChatRooomView.
     */
    public void setChatRoomView(ChatRoomView view)
    {
        _view = view;
    }   //end of setChatRoomView method

    /**
     * Sets the username to the given string.
     *
     * @param username  Holds a string of the username.
     */
    public void setUsername(String username)
    {
        _username = username;
    }   //end of setUsername method

    /**
     * Returns the chat room name as a string.
     *
     * @return  Returns the chat room name as a string.
     */
    public String getChatRoomName()
    {
        return _chatRoomName;
    }   //end of getChatRoomName method
}   //end of ChatRoom class
