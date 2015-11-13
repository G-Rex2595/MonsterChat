package wifinderinc.wifinder;

import android.content.Context;
import android.util.Log;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 *
 * Referenced a file created by Michael Young.
 */
public final class ErrorLog
{
    /**
     * Holds the name of the log to write to.
     */
    private static String logName = null;

    /**
     * Writes the error to the log.  Does nothing if
     * ErrorLog has not been initialized.
     *
     * @param e The exception to be logged.
     */
    public static synchronized void writeToLog(Exception e)
    {
        if (logName == null)
        {
            return;
        }   //end if

        Log.d("ErrorLog", e.toString() + "\tStack trace: " + Arrays.toString(e.getStackTrace()));
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(logName, true));
            writer.println(e.toString() + "\tStack trace: " + Arrays.toString(e.getStackTrace()));
            writer.close();
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try
    }   //end of writeToLog method

    /**
     * Initializes the ErrorLog.  Does nothing if
     * ErrorLog has already been initialized.
     *
     * Note:  This should be called by the homepage
     *        when the app first loads.
     *
     * @param context   Holds a reference to the activity
     */
    public static void initialize(Context context)
    {
        if (logName != null)
        {
            return;
        }   //end if

        logName = context.getFilesDir() + "/ErrorLog";
    }   //end of initialize method
}   //end of ErrorLog class
