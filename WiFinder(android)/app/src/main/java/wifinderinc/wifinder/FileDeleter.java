package wifinderinc.wifinder;

import android.content.Context;

import java.io.File;

/**
 * Created by Andrew on 12/1/2015.
 */
public final class FileDeleter
{
    public static void deleteAllFiles(Context context)
    {
        new File(context.getFilesDir() + "/logs").delete();
        new File(context.getFilesDir() + "/BlockedUsers").delete();
    }
}
