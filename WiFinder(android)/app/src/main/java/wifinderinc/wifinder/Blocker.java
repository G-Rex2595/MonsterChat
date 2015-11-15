package wifinderinc.wifinder;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public final class Blocker
{
    /**
     * Holds the name of the file.
     */
    private static String filename = null;

    /**
     * Initializes the Blocker.
     *
     * @param context   Holds a reference to the activity.
     */
    public static void initialize(Context context)
    {
        if (filename != null)
        {
            return;
        }   //end if

        filename = context.getFilesDir() + "/BlockedUsers";
    }   //end of initialize method

    /**
     * Blocks the given user by adding them to the
     * BlockedUsers file if they are not already added.
     *
     * @param user  Holds the user to be blocked.
     */
    public static void block(BlockedUser user)
    {
        if (filename == null)
        {
            return;
        }   //end if

        try
        {
            boolean isFound = false;
            if (new File(filename).exists())
            {
                BufferedReader reader = new BufferedReader(new FileReader(filename));

                while (true) {
                    String line = reader.readLine();

                    if (line == null) {
                        break;
                    }   //end if

                    int indexOfFirstSpace = line.indexOf(' ');
                    String id = line.substring(indexOfFirstSpace + 1, line.indexOf(' ', indexOfFirstSpace + 1));

                    if (id.equals(user.getId())) {
                        isFound = true;
                        break;
                    }   //end if
                }   //end while
                reader.close();
            }

            if (!isFound)
            {
                PrintWriter writer = new PrintWriter(new FileWriter(filename, true));

                writer.print(user.getUsername() + " ");
                writer.print(user.getId() + " ");
                writer.println(user.getTime());

                writer.close();
            }   //end if
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try
    }   //end of block method

    /**
     * Unblocks the give user by removing them
     * from the BlockedUsers file.
     *
     * @param user  Holds the user to be unblocked.
     */
    public static void unblock(BlockedUser user)
    {
        if (filename == null)
        {
            return;
        }   //end if

        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(filename + ".tmp", false));
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            while (true)
            {
                String line = reader.readLine();

                if (line == null)
                {
                    break;
                }   //end if

                int indexOfFirstSpace = line.indexOf(' ');
                String username = line.substring(0, indexOfFirstSpace);
                String id = line.substring(indexOfFirstSpace + 1, line.indexOf(' ', indexOfFirstSpace + 1));
                long time = Long.parseLong(line.substring(line.indexOf(' ', indexOfFirstSpace + 1) + 1));

                if (!id.equals(user.getId()))
                {
                    writer.print(username + " ");
                    writer.print(id + " ");
                    writer.println(time);
                }   //end if
            }   //end while
            reader.close();
            writer.close();

            File oldFile = new File(filename);
            oldFile.delete();
            new File(filename + ".tmp").renameTo(oldFile);
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try
    }   //end of unblock method

    /**
     * Returns a list of currently blocked users.
     *
     * @return  Returns a list of currently blocked users.
     */
    public static ArrayList<BlockedUser> getBlockedUsers()
    {
        if (filename == null)
        {
            return null;
        }   //end if

        ArrayList<BlockedUser> users = new ArrayList<BlockedUser>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            while (true)
            {
                String line = reader.readLine();

                if (line == null)
                {
                    break;
                }   //end if

                int indexOfFirstSpace = line.indexOf(' ');
                String username = line.substring(0, indexOfFirstSpace);
                String id = line.substring(indexOfFirstSpace + 1, line.indexOf(' ', indexOfFirstSpace + 1));
                long time = Long.parseLong(line.substring(line.indexOf(' ', indexOfFirstSpace + 1) + 1));
                users.add(new BlockedUser(username, id, time));
            }   //end while
            reader.close();
        }
        catch(Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try

        return users;
    }   //end of getBlockedUsers method
}   //end of Blocker class
