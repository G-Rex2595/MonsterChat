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
     * Holds a string representation of the name of the log.
     */
    private final String logName;

    /**
     * ChatLogReader constructor initializes fields.
     *
     * @param logName Holds a string representation of the name of the log.
     */
    public ChatLogReader(String logName)
    {
        this.logName = logName;
    }   //end of ChatLogReader constructor

    /**
     * Returns a list of messages read from the given log file.
     *
     * @return Returns a list of messages.
     */
    public LinkedList<Message> getMessages()
    {
        LinkedList<Message> messages = new LinkedList<Message>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(logName));

            while (true)
            {
                String line = reader.readLine();
                if (line == null)
                {
                    break;
                }   //end if

                int indexOfFirstSpace = line.indexOf(' ');
                String username = line.substring(0, indexOfFirstSpace);
                String message = line.substring(line.indexOf(' ', indexOfFirstSpace + 1) + 1);
                String id = "";
                String roomName = logName.substring(logName.lastIndexOf('/') + 1, logName.indexOf('-'));

                messages.add(new Message(username, message, id, roomName));
            }   //end while
            reader.close();
        }
        catch (Exception exception)
        {

        }   //end try

        return messages;
    }   //end of getMessages method
}   //end of ChatLogReader class
