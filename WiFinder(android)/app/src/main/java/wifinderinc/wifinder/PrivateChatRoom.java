package wifinderinc.wifinder;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class PrivateChatRoom extends ChatRoom
{
    /**
     * Holds the password of the private chat room.
     */
    private final String password;

    /**
     * PrivateChatRoom constructor initializes fields.
     *
     * @param manager   Contains a reference to the P2PManager
     * @param username  Holds a string of the username.
     * @param roomName  Holds a string of the chat room name.
     * @param activity  Holds a reference to the activity.
     * @param password  Holds the password of the private chat room.
     */
    public PrivateChatRoom(P2PManager manager, String username, String roomName, Activity activity, String password)
    {
        super(manager, username, roomName, activity);
        this.password = password;
    }   //end of PrivateChatRoom constructor

    /**
     * Adds the message received from the ChatRoomView to the message list.
     * Calls P2PManager's sendMessage to send a message.
     *
     * @param str   Holds the message to be saved and sent.
     * @param picture   Holds the picture to be saved and sent.
     */
    public void sendMessage(String str, Bitmap picture)
    {
        Message message = new Message(this.username, str, this.id, this.chatRoomName, picture);
        this.messages.add(message);
        this.view.addMessage(message);
        this.chatLogWriter.addToBuffer(message);

        message = new Message(this.username, str, this.id, this.chatRoomName + "-" + this.password, picture);
        this.manager.sendMessage(message);
    }   //end of sendMessage method
}   //end of PrivateChatRoom class
