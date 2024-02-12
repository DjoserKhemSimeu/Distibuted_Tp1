import java.rmi.*;

public interface Hello extends Remote {
	public String sayHello(Info_itf clientName)  throws RemoteException;
}
