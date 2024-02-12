import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryImpl implements Registry_itf {

    private HashMap<Accounting_itf,String> clients;

    public RegistryImpl() throws RemoteException {
        super();
        this.clients = new HashMap<>();
    }

    @Override
    public void register(Accounting_itf client, String funct) throws RemoteException {
            clients.put(client,funct);
            System.out.println("Client registered");
        
    }
    public void send(String m,Registry registry)throws RemoteException{
        for(Map.Entry<Accounting_itf,String> entry : clients.entrySet()){
            Accounting_itf key = entry.getKey();
            String value= entry.getValue();
            ClientManager_itf clientS=(ClientManager_itf) registry.lookup(value);
            clientS.recv(m);
        }
    }
    
}
