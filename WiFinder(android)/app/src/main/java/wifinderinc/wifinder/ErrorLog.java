package wifinderinc.wifinder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * The ErrorLog class will be used to create an error log file for reporting
 * bugs.
 * @author Michael Young
 */
public final class ErrorLog {
    private BufferedWriter bw = null;
	private static ErrorLog errorLogger;
	
	/**
     * Initiate error logging. Creates the output stream and keeps the file
     * open to continuously write errors.
     */
	private ErrorLog() {
		try {
            bw = new BufferedWriter(new FileWriter("error.log", true));
        }
        catch (IOException e) {
            //This should never happen, but it could so.... TODO: something
            bw = null;
        }
	}
    
    /**
     * Write to the log. This writes the line that it occurred at and the actual
     * exception that happened.
     * @param e The exception being logged (error)
     */
    public synchronized void writeToLog(Exception e) {
        if (bw != null) {
            try {
                bw.append(e.toString() + "\tStack trace: " + Arrays.toString(e.getStackTrace()) + "\n");
                bw.flush();
            }
            catch (IOException ex) {
				//TODO: something?
            }
        }
    }
    
    /**
     * Close error logging. If it can't be closed, report it (because we're
     * logging errors that's why.)
     */
    public void closeErrorLog() {
        if (bw == null) return; //It was either already closed or never initiated.
        try {
            bw.close();
        }
        catch (IOException e) {
            ErrorLog.getLogger().writeToLog(e);
            bw = null; //Couldn't close it, so I guess just null it out?
        }
    }

    /**
     * Get the error logger for logging purposes.
     * @return The error logger object.
     */
	public static ErrorLog getLogger() {
		if (errorLogger == null) errorLogger = new ErrorLog();
		return errorLogger;
	}
}
