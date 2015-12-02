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

        if (this.directory == null || !this.directory.isDirectory())
        {
            return logs;
        }   //end if

        for (File file : this.directory.listFiles())
        {
            if (file.isFile())
            {
                String fileName = file.getName();
                String chatRoomName = fileName.substring(0, fileName.indexOf('-'));
                long date = Long.parseLong(fileName.substring(fileName.indexOf('-') + 1));
                fileName = file.getAbsolutePath();

                logs.add(new LoggedChat(chatRoomName, date, fileName));
            }   //end if
        }   //end for

        return logs;
    }   //end of getLogs method
}   //end of LogManager class
