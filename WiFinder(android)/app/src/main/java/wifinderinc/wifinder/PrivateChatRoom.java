package wifinderinc.wifinder;

import android.app.Activity;
import android.graphics.Bitmap;

import java.util.ArrayList;

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
        this.password = Security.hash(password);
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
        if (this.filter.isSpam(str))
        {
            return;
        }   //end if

        Message message = new Message(this.username, str, this.id, this.chatRoomName, picture);
        this.messages.add(message);
        this.view.addMessage(message);
        this.chatLogWriter.addToBuffer(message);

        Message formattedMessage = new Message(this.username, str, this.id, this.chatRoomName + "-" + this.password, picture);
        formattedMessage.setTime(message.getTime());
        formattedMessage = Security.encrypt(formattedMessage, this.password);
        this.manager.sendMessage(formattedMessage);
    }   //end of sendMessage method

    /**
     * Adds the message received from the P2PManager to the message list.
     * Calls ChatRoomView's addMessage to add a message.
     *
     * @param message   Holds the message to be saved and added.
     */
    public void addMessage(Message message)
    {
        if ((message = Security.decrypt(message, this.password)) == null)
        {
            return;
        }   //end if

        ArrayList<BlockedUser> users = Blocker.getBlockedUsers();

        for (BlockedUser u : users)
        {
            if (u.getId().equals(message.getID()))
            {
                return;
            }   //end if
        }   //end for

        this.messages.add(message);
        this.view.addMessage(message);
        this.chatLogWriter.addToBuffer(message);
    }   //end of addMessage method
}   //end of PrivateChatRoom class
