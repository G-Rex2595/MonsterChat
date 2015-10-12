package wifinderinc.wifinder;

import java.util.LinkedList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class ChatRoomManager
{
    /**
     * Holds a string of the username.
     */
    private String _username;

    /**
     * Keeps track of the current chat room the user is in.
     */
    private ChatRoom _currentRoom;

    /**
     * Contains reference to the P2PManager.
     */
    private P2PManager _manager;

    /**
     * ChatRoomManager constructor initializes fields.
     *
     * @param username  Holds a string of the username.
     */
    public ChatRoomManager(String username)
    {
        _username = username;
        _manager = P2PManager.getInstance();
        _currentRoom = null;
    }   //end of ChatRoomManager constructor

    /**
     * Returns a LinkedList of the available chat rooms.
     *
     * @return  Returns a LinkedList of the available chat rooms.
     */
    public LinkedList<String> getAvailableRooms()
    {
        return _manager.getAvailableRooms();
    }   //end of getAvailablRooms method

    /**
     * Sets the username to the given string.
     *
     * @param username  Holds a string of the new username.
     */
    public void setUsername(String username)
    {
        _username = username;

        if (_currentRoom != null)
            _currentRoom.setUsername(username);
    }   //end of setUsername method

    /**
     * Joins a chat room and closes an old one if it exists.
     *
     * @param roomName  Holds the name of the chat room.
     */
    public void joinRoom(String roomName)
    {
        if (_currentRoom != null)
            _currentRoom.close();

        _currentRoom = new ChatRoom(_manager, _username, roomName);
    }   //end of joinRoom method

    /**
     * TODO:  Finish in a later sprint
     *
     * @param roomName
     */
    public void joinPrivateRoom(String roomName)
    {
        //join PrivateChatRoom
    }   //end of joinPrivateRoom method

    /**
     * TODO:  Finish in a later sprint
     */
    public void close()
    {
        _manager.close();
        _currentRoom.close();
    }   //end of close method

    /**
     * Returns the current chat room object.
     *
     * @return Returns the current chat room object.
     */
    public ChatRoom getCurrentChatRoom()
    {
        return _currentRoom;
    }
}   //end of ChatRoomManager class
