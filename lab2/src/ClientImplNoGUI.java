import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImplNoGUI extends UnicastRemoteObject implements Client_interface {

    private String name;

    public ClientImplNoGUI(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void receive(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }
}