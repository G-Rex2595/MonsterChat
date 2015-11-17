package wifinderinc.wifinder;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
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

    /**
     * Holds the maximum amount of space allotted
     * to saving chat logs.
     */
    private static final long MAX_SPACE = 1073741824;   //1 GB

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
     * Username ID Time Message
     *
     * Also delete old logs to free up space.
     */
    private void writeToLog()
    {
        if (this.messageBuffer.size() == 0)
        {
            return;
        }    //end if

        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(this.logName, true));

            for (Message message : this.messageBuffer)
            {
                writer.print(message.getName() + " ");
                writer.print(message.getID() + " ");
                writer.print(message.getTime() + " ");
                writer.println(message.getMessage());
            }    //end for

            writer.close();
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }    //end try

        long totalSize = 0;
        File directory = new File(this.logName.substring(0, this.logName.lastIndexOf('/')));
        File[] files = directory.listFiles();
        Arrays.sort(files, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });

        for (File file : files)
        {
            totalSize += file.length();
        }   //end for

        for (int x = 0; totalSize > MAX_SPACE && x < files.length; x += 1)
        {
            totalSize -= files[x].length();
            files[x].delete();
        }   //end for
    }   //end of writeToLog method
}   //end of ChatLogWriter Class
