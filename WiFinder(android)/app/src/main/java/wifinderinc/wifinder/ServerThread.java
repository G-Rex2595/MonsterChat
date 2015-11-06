package wifinderinc.wifinder;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Michael
 */
public class ServerThread extends Thread {

    private int port;
    private P2PManager p2PManager;
    private ServerSocket ss;

    public ServerThread(P2PManager p2PManager, int port) {
        this.p2PManager = p2PManager;
        this.port = port;
        ss = null;
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            while (!isInterrupted()) {
                final Socket s = ss.accept();
                Log.d("Server Thread", "Connection started, dispatching thread");
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Server dispatch thread", "Running");
                            //ObjectInputStream read = new ObjectInputStream(s.getInputStream());
                            //ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
                            //output.writeObject(new Message("ServerThread", "LOL U DIED", null, "Global"));
                            //p2pmanager.addConnection(output);
                            Log.d("Server dispatch thread", "Added connection");
                            p2PManager.addConnection(new ObjectOutputStream(s.getOutputStream()));
                            ObjectInputStream read = new ObjectInputStream(s.getInputStream());
                            while (s.isConnected()) {
                                Log.d("Server dispatch thread", "Blocking on readObject");
                                //Message m = (Message) read.readObject();
                                Message m = (Message) read.readObject();
                                Log.d("Server dispatch thread", "Received message " + m.getMessage());
                                p2PManager.receiveMessage(m);
                            }
                            Log.d("Serverthread", "Left Socket Loop");
                            s.close();
                            read.close();
                            //We only need to close the read end. The P2PManager will close all write ends.
                            //read.close();
                        }
                        catch (IOException e) {

                        }
                        catch (ClassNotFoundException cnfe) {

                        }
                    }
                };
                t.start();
            }
        } catch (IOException e) {
            //TODO error handle
        }
    }

    public void close() {
        interrupt();
        try {
            if (ss != null) ss.close();
        }
        catch (IOException e) { /*Is there really anything to do? */ }
        ss = null;
    }
}
