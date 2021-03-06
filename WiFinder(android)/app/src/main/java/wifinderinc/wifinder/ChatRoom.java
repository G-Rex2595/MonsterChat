package wifinderinc.wifinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class ChatRoom
{
    /**
     * Contains a reference to the P2PManager
     */
    protected P2PManager manager;

    /**
     * Holds a string of the username.
     */
    protected String username;

    /**
     * Holds a string of the chat room name.
     */
    protected String chatRoomName;

    /**
     * Contains a reference to the ChatRoomView.
     */
    protected ChatRoomView view;

    /**
     * Holds a list of Messages.
     */
    protected LinkedList<Message> messages;

    /**
     * Holds a string of the id.
     */
    protected String id;

    /**
     * Contains a reference to the chat room's log writer
     */
    protected ChatLogWriter chatLogWriter;

    /**
     * Holds the reference to the SpamFilter.
     */
    protected final SpamFilter filter;

    /**
     * ChatRoom constructor initializes fields.
     *
     * @param manager   Contains a reference to the P2PManager
     * @param username  Holds a string of the username.
     * @param roomName  Holds a string of the chat room name.
     * @param activity  Holds a reference to the activity.
     */
    public ChatRoom(P2PManager manager, String username, String roomName, Activity activity)
    {
        this.manager = manager;
        this.username = username;
        this.chatRoomName = roomName;
        this.messages = new LinkedList<Message>();
        this.id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        this.chatLogWriter = new ChatLogWriter(activity, roomName);
        this.filter = new SpamFilter();
    }   //end of ChatRoom constructor

    /**
     * Returns a list of Messages.
     *
     * @return  Returns a list of Messages.
     */
    public LinkedList<Message> getMessages()
    {
        return this.messages;
    }   //end of getMessages method

    /**
     * Adds the message received from the ChatRoomView to the message list.
     * Calls P2PManager's sendMessage to send a message.
     *
     * @param str       Holds the message to be saved and sent.
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

        message = Security.encrypt(message, null);
        this.manager.sendMessage(message);
    }   //end of sendMessage method

    /**
     * Adds the message received from the P2PManager to the message list.
     * Calls ChatRoomView's addMessage to add a message.
     *
     * @param message   Holds the message to be saved and added.
     */
    public void addMessage(Message message)
    {
        if ((message = Security.decrypt(message, null)) == null)
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

        message.setTime();
        this.messages.add(message);
        this.view.addMessage(message);
        this.chatLogWriter.addToBuffer(message);
    }   //end of addMessage method

    /**
     * Calls ChatRoomView's failedConnect.
     */
    public void failedConnect()
    {
        //_view.failedConnect();
    }   //end of failedConnect method

    /**
     * Closes the chat room's log writer.
     */
    public void close()
    {
        this.chatLogWriter.close();
    }   //end of close method

    /**
     * Sets the view to the given ChatRoomView.
     *
     * @param view  Contains a reference to the ChatRooomView.
     */
    public void setChatRoomView(ChatRoomView view)
    {
        this.view = view;
    }   //end of setChatRoomView method

    /**
     * Sets the username to the given string.
     *
     * @param username  Holds a string of the username.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }   //end of setUsername method

    /**
     * Returns the chat room name as a string.
     *
     * @return  Returns the chat room name as a string.
     */
    public String getChatRoomName()
    {
        return this.chatRoomName;
    }   //end of getChatRoomName method
}   //end of ChatRoom class
