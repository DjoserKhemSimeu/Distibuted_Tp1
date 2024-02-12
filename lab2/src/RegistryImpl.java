import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryImpl implements Registry_itf {

    private List<Accounting_itf> clients;

    public RegistryImpl() throws RemoteException {
        super();
        this.clients = new ArrayList<>();
    }

    @Override
    public void register(Accounting_itf client) throws RemoteException {
            clients.add(client);
            System.out.println("Client registered");
        
    }
}
