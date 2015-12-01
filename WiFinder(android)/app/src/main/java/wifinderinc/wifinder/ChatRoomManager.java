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
    private ChatRoom _currentChatRoom;

    /**
     * Contains reference to the P2PManager.
     */
    private P2PManager _manager;

    /**
     * Contains a reference to the activity
     */
    private Activity _activity;

    /**
     * ChatRoomManager constructor initializes fields.
     *
     * @param username  Holds a string of the username.
     * @param activity  Holds the app's activity object.
     */
    public ChatRoomManager(String username, Activity activity)
    {
        _username = username;
        _manager = P2PManager.getInstance(activity);
        _currentChatRoom = null;
        _activity = activity;
    }   //end of ChatRoomManager constructor

    /**
     * Returns a LinkedList of the available chat rooms.
     *
     * @return  Returns a LinkedList of the available chat rooms.
     */
    public LinkedList<String> getAvailableRooms()
    {
        LinkedList<String> rooms = _manager.getAvailableRooms();

        for (int x = 0; x < rooms.size(); x++)
        {
            if (rooms.get(x).contains("-"))
            {
                rooms.set(x, rooms.get(x).substring(0, rooms.get(x).indexOf('-')));
            }   //end if
        }   //end for

        return rooms;
    }   //end of getAvailableRooms method

    /**
     * Returns a list of the available chat rooms with
     * their passwords still intact.
     *
     * @return  Returns a list of available chat rooms.
     */
    private LinkedList<String> getUnformattedRooms()
    {
        return _manager.getAvailableRooms();
    }   //end of getUnformattedRooms method

    /**
     * Sets the username to the given string.
     *
     * @param username  Holds a string of the new username.
     */
    public void setUsername(String username)
    {
        _username = username;

        if (_currentChatRoom != null)
            _currentChatRoom.setUsername(username);
    }   //end of setUsername method

    /**
     * Joins a chat room and closes an old one if it exists.
     *
     * @param roomName  Holds the name of the chat room.
     * @param password  Holds the password used to try to enter the chat room.
     */
    public void joinRoom(String roomName, String password)
    {
        if (_currentChatRoom != null)
        {
            _currentChatRoom.close();
            _currentChatRoom = null;
        }

        if (password != null)
        {
            LinkedList<String> rooms = getUnformattedRooms();
            password = Security.hash(password);

            for (String s : rooms)
            {
                Log.d("RoomName", s);
                if (s.contains("-") && s.substring(0, s.indexOf('-')).equals(roomName))
                {
                    Log.d("Manager", "**** THIS WAY ****");
                    joinPrivateRoom(roomName, password, s.substring(s.indexOf('-') + 1));
                    return;
                }   //end if
            }   //end for

            Log.d("HERHEHRE", password);
            joinPrivateRoom(roomName, password, password);
            return;
        }   //end if

        _currentChatRoom = new ChatRoom(_manager, _username, roomName, _activity);
        _manager.setChatRoom(_currentChatRoom);
    }   //end of joinRoom method

    /**
     * Joins a private chat room and closes the old chat room if it exists.
     *
     * @param roomName          Holds the name of the chat room.
     * @param password          Holds the password used to try to enter the chat room.
     * @param correctPassword   Holds the correct password.
     */
    private void joinPrivateRoom(String roomName, String password, String correctPassword)
    {
        Log.d("PrivateChatRoom", "*** We are in ***");
        Log.d("Password", correctPassword + " " + password);
        if (!correctPassword.equals(password))
        {
            Log.d("IncorrectPassword", correctPassword + " " + password);
            return;
        }   //end if

        _currentChatRoom = new PrivateChatRoom(_manager, _username, roomName, _activity, password);
        _manager.setChatRoom(_currentChatRoom);
    }   //end of joinPrivateRoom method

    /**
     * Closes the P2PManager and the current chat room.
     */
    public void close()
    {
        _manager.close();
        if(_currentChatRoom != null)
            _currentChatRoom.close();
    }   //end of close method

    /**
     * Lets the P2PManager know the app has been reopened.
     */
    public void onResume()
    {
        _manager.registerReceiver();
    }   //end of onResume method

    /**
     * Lets the P2PManager know the app has been closed.
     */
    public void onPause()
    {
        _manager.unregisterReceiver();
    }   //end of onPause method

    /**
     * Returns a reference to the current chat room.
     *
     * @return  Returns a reference to the current chat room.
     */
    public ChatRoom getCurrentChatRoom()
    {
        return _currentChatRoom;
    }   //end of getCurrentChatRoom method
}   //end of ChatRoomManager class
