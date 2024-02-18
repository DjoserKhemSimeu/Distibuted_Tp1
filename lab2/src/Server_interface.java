import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server_interface extends Remote{
    void joinChat (Client_interface client) throws RemoteException, IOException ;
    void leaveChat (Client_interface client) throws RemoteException;
    void send(String message, String sender) throws RemoteException;
    void sendAll(String message) throws RemoteException;
    boolean isClientNameTaken(String name) throws RemoteException ;
    
    
}