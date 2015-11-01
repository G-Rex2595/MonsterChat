package wifinderinc.wifinder;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class LoggedChat
{
    /**
     * Holds the name of the chat room.
     */
    private final String chatRoomName;

    /**
     * Holds the date as a number.
     */
    private final long date;

    /**
     * Holds the name of the log.
     */
    private final String fileName;

    /**
     * LoggedChat constructor initializes fields.
     *
     * @param chatRoomName  Holds the name of the chat room.
     * @param date          Holds the date as number.
     * @param fileName      Holds the name of the log.
     */
    public LoggedChat(String chatRoomName, long date, String fileName)
    {
        this.chatRoomName = chatRoomName;
        this.date = date;
        this.fileName = fileName;
    }   //end of LoggedChat constructor

    /**
     * Returns the name of the chat room.
     *
     * @return  Returns the name of the chat room.
     */
    public String getRoomName()
    {
        return this.chatRoomName;
    }   //end of getRoomName method

    /**
     * Returns the date as a number.
     *
     * @return  Returns the date as number.
     */
    public long getDate()
    {
        return this.date;
    }   //end of getDate method

    /**
     * Returns the name of the log.
     *
     * @return  Returns the name of the log.
     */
    public String getFileName()
    {
        return this.fileName;
    }   //end of getFileName method
}   //end of LoggedChat class
