package wifinderinc.wifinder;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class ChatLogWriter
{
    /**
     * Holds the maximum number of messages stored
     * before the buffer is written to a file.
     */
    private static final int MAX_MESSAGES = 500;

    /**
     * Holds a list of messages.
     */
    private LinkedList<Message> messageBuffer;

    /**
     * Holds the name of the log to write to.
     */
    private final String logName;
    //either restrict chat room names to allow only letters and number
    //or make file names only system time

    /**
     * ChatLogWriter constructor initializes fields and
     * creates a logs directory if needed.
     *
     * @param context       Holds a reference to the activity.
     * @param chatRoomName  Holds the name of the chat room.
     */
    public ChatLogWriter(Context context, String chatRoomName)
    {
        this.messageBuffer = new LinkedList<Message>();
        new File(context.getFilesDir() + "/logs").mkdir();
        this.logName = context.getFilesDir() + "/logs/" + chatRoomName + "-" + System.currentTimeMillis();
    }   //end of ChatLogWriter constructor

    /**
     * Adds the given message to the buffer.
     * Writes the buffer to a file if full.
     *
     * @param message   Holds the given message.
     */
    public void addToBuffer(Message message)
    {
        if (this.messageBuffer.size() == this.MAX_MESSAGES)
        {
            writeToLog();
            this.messageBuffer.clear();
        }   //end if

        this.messageBuffer.add(message);
    }   //end of addToBuffer method

    /**
     * Writes the buffer to a file when the current
     * chat room is closed.
     */
    public void close()
    {
        writeToLog();
    }   //end of close method

    /**
     * Writes the buffer to a file in the following format:
     * Username Time Message
     */
    private void writeToLog()
    {
        if (this.messageBuffer.size() == 0)
        {
            return;
        }	//end if

        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(this.logName, true));

            for (Message message : this.messageBuffer)
            {
                writer.print(message.getName() + " ");
                writer.print(message.getTime() + " ");
                writer.println(message.getMessage());
            }	//end for

            writer.close();
        }
        catch (Exception exception)
        {
        }	//end try
    }   //end of writeToLog method
}   //end of ChatLogWriter Class
