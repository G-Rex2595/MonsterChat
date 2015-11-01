package wifinderinc.wifinder;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class LogManager
{
    /**
     * Holds a reference to the logs directory.
     */
    private final File directory;

    /**
     * LogManager constructor initializes fields.
     *
     * @param context   Holds a reference to the current activity.
     */
    public LogManager(Context context)
    {
        this.directory = new File(context.getFilesDir() + "/logs/");
    }   //end of LogManager constructor

    /**
     * Returns of list of logs contained in the log directory.
     *
     * @return  Returns a list of logs.
     */
    public ArrayList<LoggedChat> getLogs()
    {
        ArrayList<LoggedChat> logs = new ArrayList<LoggedChat>();

        for (File file : this.directory.listFiles())
        {
            if (file.isFile())
            {
                String fileName = file.getName();
                String chatRoomName = fileName.substring(0, fileName.indexOf('-'));
                int date = Integer.parseInt(fileName.substring(fileName.indexOf('-') + 1));
                fileName = file.getAbsolutePath();

                logs.add(new LoggedChat(chatRoomName, date, fileName));
            }   //end if
        }   //end for

        return logs;
    }   //end of getLogs method

    /**
     * Returns a log opened for reading.
     *
     * @param log   Holds a reference to a log to be opened.
     * @return  Returns a log opened for reading.
     */
    public ChatLogReader openLog(LoggedChat log)
    {
        return new ChatLogReader(log.getRoomName());
    }   //end of openLog method
}   //end of LogManager class
