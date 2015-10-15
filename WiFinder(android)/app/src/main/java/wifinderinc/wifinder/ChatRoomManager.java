package wifinderinc.wifinder;

import android.app.Activity;
import android.util.Log;

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
     * @param activity  Holds the app's activity object.
     */
    public ChatRoomManager(String username, Activity activity)
    {
        _username = username;
        _manager = new P2PManager(activity);
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
    }   //end of getAvailableRooms method

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
    public ChatRoom joinRoom(String roomName)
    {
        if (_currentRoom != null)
            _currentRoom.close();

        _currentRoom = new ChatRoom(_manager, _username, roomName);
        _manager.setChatRoom(_currentRoom);

        return _currentRoom;
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

    public void onResume()
    {
        _manager.registerReceiver();
    }   //end of onResume method

    public void onPause()
    {
        _manager.unregisterReceiver();
    }   //end of onPause method
}   //end of ChatRoomManager class
