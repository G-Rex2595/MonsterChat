package wifinderinc.wifinder;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Michael on 11/5/2015.
 */
public class ClientThread extends Thread {

    private Socket socket;
    private P2PManager p2pManager;

    public ClientThread(P2PManager p2pManager, InetAddress address) {
        Log.d("Clientthread", "Constructor" + address.toString());
        try {
            socket = new Socket(address, P2PManager.PORT);
            this.p2pManager = p2pManager;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            //ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            //p2pmanager.addConnection(output);
            boolean notAdded = true;
            while (socket.isConnected()) {
                if (notAdded)
                {
                    notAdded = false;
                    p2pManager.addConnection(new ObjectOutputStream(socket.getOutputStream()));
                }
                //Message m = (Message) read.readObject();
                Message m = (Message)new ObjectInputStream(socket.getInputStream()).readObject();
                p2pManager.receiveMessage(m);
            }
            Log.d("Clientthread", "Left Socket Loop");
            socket.close();
            //We only need to close the read end. The P2PManager will close all write ends.
            //read.close();

        }
        catch (IOException e) {
            //TODO error handle
            //Most likely cause of this is connection dropped
        } catch (ClassNotFoundException e) {
            //TODO error handle
            //If this happens, we have some major problems to consider.
            //The first case I can see this happening is two different versions of the app
            //and the message object has changed between them.
        }
    }
}