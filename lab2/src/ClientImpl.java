import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client_interface {

    private String name;
    private ClientGUI clientGUI;

    public ClientImpl(String name, ClientGUI clientGUI) throws RemoteException {
        this.name = name;
        this.clientGUI = clientGUI;
    }

    @Override
    public void receive(String message) throws RemoteException {
        if (clientGUI != null) {
            clientGUI.receiveMessage(message);
        } else {
            System.err.println("ClientGUI is null. Message not received: " + message);
        }
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }
}

