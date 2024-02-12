import java.rmi.Remote;
import java.rmi.RemoteException;
public class ClientManagerImpl implements ClientManager_itf {
    public void recv(String m){
        System.out.println(m);
    }
}