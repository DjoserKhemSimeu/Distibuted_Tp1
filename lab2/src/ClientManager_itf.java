import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ClientManager_itf extends Remote {
    public void recv(String m) throws RemoteException;
}