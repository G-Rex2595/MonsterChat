package wifinderinc.wifinder;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class Message implements Serializable
{
    /**
     * Holds a string of the username.
     */
    private String username;

    /**
     * Holds a string of the message.
     */
    private String message;

    /**
     * Holds a string of the id.
     */
    private String id;

    /**
     * Holds a long of the time.
     */
    private long time;

    /**
     * Holds a string of the chat room name.
     */
    private String chatRoomName;

    /**
     * Holds the message's picture.
     */
    private final Bitmap picture;

    /**
     * Holds the max width of the picture.
     */
    private final int MAX_PIC_WIDTH = 256;

    /**
     * Holds the max height of the picture.
     */
    private final int MAX_PIC_HEIGHT = 256;

    /**
     * Message constructor initializes the fields.
     *
     * @param username  Holds a string of the username.
     * @param message   Holds a string of the message.
     * @param id        Holds a string of the id.
     * @param roomName  Holds a string of the chat room name.
     * @param picture   Holds the message's picture.
     */
    public Message(String username, String message, String id, String roomName, Bitmap picture)
    {
        this.username = username;
        this.message = message;
        this.id = id;
        this.time = System.currentTimeMillis();
        this.chatRoomName = roomName;
        if (picture != null)
        {
            this.picture = Bitmap.createScaledBitmap(picture, this.MAX_PIC_WIDTH, this.MAX_PIC_HEIGHT, true);
        }
        else
        {
            this.picture = null;
        }   //end if
    }   //end of Message constructor

    /**
     *  Returns username as a string.
     *
     * @return  Returns the username as a string.
     */
    public String getName()
    {
        return this.username;
    }   //end of getName method

    /**
     * Returns the message as a string.
     *
     * @return  Returns the message as a string.
     */
    public String getMessage()
    {
        return this.message;
    }   //end of getMessage method

    /**
     * Returns the time as a long.
     *
     * @return  Returns the time as a long.
     */
    public long getTime()
    {
        return this.time;
    }   //end of getTime method

    /**
     * Returns the id as a string.
     *
     * @return  Returns the id as a string.
     */
    public String getID()
    {
        return this.id;
    }   //end of getID method

    /**
     * Sets the time to the given long.
     *
     * @param time  Holds the new time.
     */
    public void setTime(long time)
    {
        this.time = time;
    }   //end of setTime method

    /**
     * Sets the time to the current time in milliseconds.
     */
    public void setTime()
    {
        this.time = System.currentTimeMillis();
    }   //end of setTime method

    /**
     * Returns the chat room name as a string.
     *
     * @return  Returns the chat room name as a string.
     */
    public String getChatRoomName()
    {
        return this.chatRoomName;
    }   //end of getChatRoomName method

    /**
     * Returns the message's picture.
     *
     * @return  Returns the message's picture.
     */
    public Bitmap getPicture()
    {
        return this.picture;
    }   //end of getPicture method
}   //end of Message class