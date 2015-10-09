package wifinderinc.wifinder;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class Message
{
    /**
     * Holds a string of the username.
     */
    private String _username;

    /**
     * Holds a string of the message.
     */
    private String _message;

    /**
     * Holds a string of the id.
     */
    private String _id;

    /**
     * Holds a long of the time.
     */
    private long _time;

    /**
     * Holds a string of the chat room name.
     */
    private String _roomName;

    /**
     * Message constructor initializes the fields.
     *
     * @param username  Holds a string of the username.
     * @param message   Holds a string of the message.
     * @param id        Holds a string of the id.
     * @param roomName  Holds a string of the chat room name.
     */
    public Message(String username, String message, String id, String roomName)
    {
        _username = username;
        _message = message;
        _id = id;
        _time = 0;
        _roomName = roomName;
    }   //end of Message constructor

    /**
     *  Returns username as a string.
     *
     * @return  Returns the username as a string.
     */
    public String getName()
    {
        return _username;
    }   //end of getName method

    /**
     * Returns the message as a string.
     *
     * @return  Returns the message as a string.
     */
    public String getMessage()
    {
        return _message;
    }   //end of getMessage method

    /**
     * Returns the time as a long.
     *
     * @return  Returns the time as a long.
     */
    public long getTime()
    {
        return _time;
    }   //end of getTime method

    /**
     * Returns the id as a string.
     *
     * @return  Returns the id as a string.
     */
    public String getID()
    {
        return _id;
    }   //end of getID method

    /**
     * Sets the time to the given long.
     *
     * @param time  Holds the new time.
     */
    public void setTime(long time)
    {
        _time = time;
    }   //end of setTime method

    /**
     * Sets the time to the current time in milliseconds.
     */
    public void setTime()
    {
        _time = System.currentTimeMillis();
    }   //end of setTime method

    /**
     * Returns the chat room name as a string.
     *
     * @return  Returns the chat room name as a string.
     */
    public String getRoomName()
    {
        return _roomName;
    }   //end of getRoomName method
}   //end of Message class