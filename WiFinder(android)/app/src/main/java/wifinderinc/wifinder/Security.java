package wifinderinc.wifinder;

import android.util.Base64;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author  Andrew Sytsma <asytsma@purdue.edu>
 */
public final class Security
{
    private static final String DEFAULT_PASSWORD = "Az&37k##(*&r.-Q[";

    /**
     * Returns an encrypted message.
     *
     * @param message   Holds the message to be encrypted.
     * @param password  Holds the password of the chat room.
     * @return          Returns an encrypted message.
     */
    public static Message encrypt(Message message, String password)
    {
        if (message.getName() == null)
        {
            return null;
        }   //end if

        if (password == null)
        {
            password = hash(DEFAULT_PASSWORD);
        }   //end if

        if (password.length() > 16)
        {
            password = password.substring(0, 16);
        }
        else if (password.length() < 16)
        {
            while (password.length() < 16)
            {
                password += '\0';
            }   //end while
        }   //end if

        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(password.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(password.getBytes("UTF-8"));

            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            String username = new String(Base64.encode(cipher.doFinal(message.getName().getBytes("UTF-8")), Base64.DEFAULT), "UTF-8");
            String msg = new String(Base64.encode(cipher.doFinal(message.getMessage().getBytes("UTF-8")), Base64.DEFAULT), "UTF-8");
            String id = new String(Base64.encode(cipher.doFinal(message.getID().getBytes("UTF-8")), Base64.DEFAULT), "UTF-8");

            Message m = new Message(username, msg, id, message.getChatRoomName(), message.getPicture());
            m.setTime(message.getTime());

            return m;
        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try

        return null;
    }   //end of encrypt method

    /**
     * Returns a message after it has been decrypted.
     * Returns null if the wrong password is given.
     *
     * @param message   Holds the encrypted message.
     * @param password  Holds the password of the chat room.
     * @return          Returns a message after it has been decrypted.
     */
    public static Message decrypt(Message message, String password)
    {
        if (password == null)
        {
            password = hash(DEFAULT_PASSWORD);
        }   //end if

        if (password.length() > 16)
        {
            password = password.substring(0, 16);
        }
        else if (password.length() < 16)
        {
            while (password.length() < 16)
            {
                password += '\0';
            }   //end while
        }   //end if

        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(password.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(password.getBytes("UTF-8"));

            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            String username = new String(cipher.doFinal(Base64.decode(message.getName(), Base64.DEFAULT)));
            String msg = new String(cipher.doFinal(Base64.decode(message.getMessage(), Base64.DEFAULT)));
            String id = new String(cipher.doFinal(Base64.decode(message.getID(), Base64.DEFAULT)));

            Message m = new Message(username, msg, id, message.getChatRoomName(), message.getPicture());
            m.setTime(message.getTime());

            return m;
        }
        catch (BadPaddingException exception)
        {

        }
        catch (Exception exception)
        {
            ErrorLog.writeToLog(exception);
        }   //end try

        return null;
    }   //end of decrypt method

    /**
     * Returns the password after it has been hashed.
     * Returns null if the password is null.
     *
     * @param password  Holds the password to be hashed.
     * @return          Returns the password after it has been hased.
     */
    public static String hash(String password)
    {
        if (password == null)
        {
            return null;
        }   //end if

        return password.hashCode() + "";
    }   //end of hash method
}   //end of Security class
