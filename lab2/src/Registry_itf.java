import java.rmi.*;
import java.rmi.registry.*;

public interface Registry_itf extends Remote {
    public void register(Accounting_itf client, String funct) throws RemoteException;
    public void send(String m,Registry registry)throws RemoteException;
}
