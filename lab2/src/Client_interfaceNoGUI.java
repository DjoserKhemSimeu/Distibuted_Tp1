import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client_interfaceNoGUI extends Remote {
    void receive(String message) throws RemoteException;
    String getName() throws RemoteException;
}