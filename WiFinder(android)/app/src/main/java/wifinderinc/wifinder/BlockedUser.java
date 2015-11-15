package wifinderinc.wifinder;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class BlockedUser
{
    /**
     * Holds the name of the user.
     */
    private final String username;

    /**
     * Holds the id of the user.
     */
    private final String id;

    /**
     * Holds the time the user was blocked.
     */
    private final long time;

    /**
     * BlockedUser constructor initializes values.
     *
     * @param username  Holds the name of the user.
     * @param id        Holds the id of the user.
     * @param time      Holds the time the user was blocked.
     */
    public BlockedUser(String username, String id, long time)
    {
        this.username = username;
        this.id = id;
        this.time = time;
    }   //end of BlockedUser constructor

    /**
     * Returns the user's name.
     *
     * @return  Returns the user's name.
     */
    public String getUsername()
    {
        return this.username;
    }   //end of getUsername method

    /**
     * Returns the user's id.
     *
     * @return  Returns the user's id.
     */
    public String getId()
    {
        return this.id;
    }   //end of getId

    /**
     * Returns the time the user was blocked.
     *
     * @return  Returns the time the user was blocked.
     */
    public long getTime()
    {
        return this.time;
    }   //end of getTime method
}   //end of BlockedUser class
