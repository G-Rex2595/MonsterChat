package wifinderinc.wifinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class ChatLogReader
{
    /**
     * Holds a reference to the current log file.
     */
    private BufferedReader reader;

    /**
     * Holds the location of the current line.
     */
    private int currentLine;

    /**
     * Holds the maximum number of messages
     * allowed to be returned in a list.
     */
    private final int MAX_MESSAGES = 500;

    /**
     * Holds the name of the current log.
     */
    private final String logName;

    /**
     * ChatLogReader constructor initializes fields.
     *
     * @param logName Holds a string representation of the name of the log.
     */
    public ChatLogReader(String logName)
    {
        try
        {
            this.reader = new BufferedReader(new FileReader(logName));
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try

        this.currentLine = 0;
        this.logName = logName;
    }   //end of ChatLogReader constructor

    /**
     * Returns a list of messages from the log containing the next
     * block of messages.  The amount of messages returned is less
     * than or equal to the maximum specified value.
     *
     * @return  Returns a list of messages.
     */
    public LinkedList<Message> getNewerMessages()
    {
        LinkedList<Message> messages = new LinkedList<Message>();

        for (int x = 0; x < this.MAX_MESSAGES; x += 1)
        {
            try
            {
                String line = this.reader.readLine();
                if (line == null)
                {
                    break;
                }   //end if

                int indexOfFirstSpace = line.indexOf(' ');
                String username = line.substring(0, indexOfFirstSpace);
                String message = line.substring(line.indexOf(' ', indexOfFirstSpace + 1) + 1);
                String id = "";
                String roomName = this.logName.substring(this.logName.lastIndexOf('/') + 1, this.logName.indexOf('-'));

                messages.add(new Message(username, message, id, roomName));
                this.currentLine += 1;
            }
            catch (Exception exception)
            {
                ErrorLog.writeToLog(exception);
            }   //end try
        }   //end for

        return messages;
    }   //end of getNewerMessages method

    /**
     * Returns a list of messages from the log containing the previous
     * block of messages.  The amount of messages returned is equal
     * to the maximum specified value.
     *
     * If an error occurs, null is returned.
     *
     * @return  Returns a list of messages.
     * @error Returns null.
     */
    public LinkedList<Message> getOlderMessages()
    {
        if (this.currentLine < this.MAX_MESSAGES * 2)
        {
            return null;
        }   //end if

        try
        {
            this.reader.close();
            this.reader = new BufferedReader(new FileReader(this.logName));

            for (int x = 0; this.currentLine - x != this.MAX_MESSAGES * 2; x++)
            {
                this.reader.readLine();
            }   //end for
            this.currentLine -= this.MAX_MESSAGES * 2;

            return getNewerMessages();
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try

        return null;
    }   //end of getOlderMessages method

    /**
     * Closes the currently opened log reader.
     */
    public void close()
    {
        try
        {
            this.reader.close();
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try
    }   //end of close method
}   //end of ChatLogReader class
