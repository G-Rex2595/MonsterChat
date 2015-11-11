package wifinderinc.wifinder;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public class Settings
{
    /**
     * Holds the GUI color setting.
     */
    private int guiColor;

    /**
     * Holds the font setting.
     */
    private int font;

    /**
     * Holds the style setting.
     */
    private int style;

    /**
     * Holds the username setting.
     */
    private String username;

    /**
     * Holds the timestamp setting.
     */
    private int timestamp;

    /**
     * Holds the military time setting.
     */
    private int militaryTime;

    /**
     * Holds the name of the settings file.
     */
    private final String filename;

    /**
     * Settings constructor initializes fields.
     *
     * @param context   Holds a reference to the current view.
     */
    public Settings(Context context)
    {
        this.filename = context.getFilesDir() + "/settings";

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(this.filename));
            this.guiColor = Integer.parseInt(reader.readLine());
            this.font = Integer.parseInt(reader.readLine());
            this.style = Integer.parseInt(reader.readLine());
            this.username = reader.readLine();
            this.timestamp = Integer.parseInt(reader.readLine());
            this.militaryTime = Integer.parseInt(reader.readLine());
            reader.close();
        }
        catch (FileNotFoundException exception)
        {
            this.guiColor = 0;
            this.font = 0;
            this.style = 0;
            this.username = "";
            this.timestamp = 0;
            this.militaryTime = 0;
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try
    }	//end of Settings constructor

    /**
     * Sets the GUI color to the given setting.
     *
     * @param setting   Holds the value of the new setting.
     */
    public void setGuiColor(int setting)
    {
        this.guiColor = setting;
    }	//end of setGuiColor method

    /**
     * Returns the current GUI color setting.
     *
     * @return  Returns the GUI color setting.
     */
    public int getGuiColor()
    {
        return this.guiColor;
    }	//end of getGuiColor method

    /**
     * Sets the font to the given setting.
     *
     * @param setting   Holds the value of the font.
     */
    public void setFont(int setting)
    {
        this.font = setting;
    }	//end of setFont method

    /**
     * Returns the current font setting.
     *
     * @return  Returns the font setting.
     */
    public int getFont()
    {
        return this.font;
    }	//end of getFont method

    /**
     * Sets the style to the given setting.
     *
     * @param setting   Holds the value of the style.
     */
    public void setStyle(int setting)
    {
        this.style = setting;
    }	//end of setStyle method

    /**
     * Returns the current style setting.
     *
     * @return  Returns the style setting.
     */
    public int getStyle()
    {
        return this.style;
    }	//end of getStyle method

    /**
     * Sets the current username to the given setting.
     *
     * @param setting   Holds the value of the username.
     */
    public void setUsername(String setting)
    {
        this.username = setting;
    }   //end of setUsername method

    /**
     * Returns the current username setting.
     *
     * @return  Returns the username setting.
     */
    public String getUsername()
    {
        return this.username;
    }   //end of getUsername method

    /**
     * Sets the current timestamp to the given setting.
     *
     * @param setting   Holds the value of the timestamp.
     */
    public void setTimestamp(int setting)
    {
        this.timestamp = setting;
    }   //end of setTimestamp method

    /**
     * Returns the current timestamp setting.
     *
     * @return  Returns the timestamp setting.
     */
    public int getTimestamp()
    {
        return this.timestamp;
    }   //end of getTimestamp method

    /**
     * Sets the current military time to the given setting.
     *
     * @param setting   Holds the value of the military time.
     */
    public void setMilitaryTime(int setting)
    {
        this.militaryTime = setting;
    }   //end of setMilitaryTime method

    /**
     * Returns the current military timestamp setting.
     *
     * @return  Returns the military timestamp setting.
     */
    public int getMilitaryTime()
    {
        return this.militaryTime;
    }   //end of getMilitaryTime method

    /**
     * Writes the current settings to the settings file.
     */
    public void writeToFile()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(this.filename, false));
            writer.println(this.guiColor);
            writer.println(this.font);
            writer.println(this.style);
            writer.println(this.username);
            writer.println(this.timestamp);
            writer.println(this.militaryTime);
            writer.close();
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try
    }   //end of writeToFile method
}   //end of Settings class
