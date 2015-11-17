package wifinderinc.wifinder;

import android.util.Log;

import java.util.ArrayList;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class SpamFilter
{
    /**
     * Holds the last messages sent up to the
     * maximum count.
     */
    private ArrayList<String> lastMessages;

    /**
     * Holds the maximum number of old messages.
     */
    private final int MAX_OLD_MESSAGSES = 3;

    /**
     * Holds the time that the last message was sent.
     */
    private long lastSentTime;

    /**
     * Holds the minimum time required to reset the
     * spam filter.
     */
    private long RESET_INTERVAL = 3000;

    /**
     * Holds the minimum time between sends to not be
     * considered spam.
     */
    private long MIN_SEND_TIME = 250;

    /**
     * SpamFilter constructor initializes fields.
     */
    public SpamFilter()
    {
        this.lastMessages = new ArrayList<String>();
    }   //end of SpamFilter constructor

    /**
     * Returns whether or not the message being sent is spam.
     * True  = It is spam.
     * False = It is not spam.
     *
     * @param str   Holds the message being sent.
     * @return      Returns whether or not the message being sent is spam.
     */
    public boolean isSpam(String str)
    {
        long currentSentTime = System.currentTimeMillis();

        if (this.lastMessages.size() != 0 && currentSentTime - this.lastSentTime < MIN_SEND_TIME)
        {
            Log.d("Spam", "" + (currentSentTime - this.lastSentTime));

            if (this.lastMessages.size() == this.MAX_OLD_MESSAGSES)
            {
                this.lastMessages.remove(0);
            }   //end if

            this.lastMessages.add(str);
            return true;
        }   //end if

        if (this.lastMessages.size() != 0 && currentSentTime - this.lastSentTime >= this.RESET_INTERVAL)
        {
            this.lastMessages.clear();
        }   //end if

        this.lastSentTime = currentSentTime;

        boolean result = false;
        if (this.lastMessages.size() == this.MAX_OLD_MESSAGSES)
        {
            result = true;
            for (int x = 1; x < this.MAX_OLD_MESSAGSES; x++)
            {
                if (!this.lastMessages.get(x - 1).equals(this.lastMessages.get(x)))
                {
                    result = false;
                    break;
                }   //end if
            }   //end for

            if (result && !this.lastMessages.get(0).equals(str))
            {
                result = false;
            }   //end if

            this.lastMessages.remove(0);
        }   //end if

        this.lastMessages.add(str);
        return result;
    }   //end of isSpam method
}   //end of SpamFilter class
