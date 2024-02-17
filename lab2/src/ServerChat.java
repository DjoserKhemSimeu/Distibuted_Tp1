import java.rmi.*; 
import java.rmi.server.*; 
import java.rmi.registry.*;

public class ServerChat {

    public static void  main(String [] args) {
        try {
            // Create a Hello remote object
            ServerImpl s = new ServerImpl();
            Server_interface s_stub = (Server_interface) UnicastRemoteObject.exportObject(s, 0);

            Registry registry = null;
            if (args.length > 0)
                registry = LocateRegistry.getRegistry(Integer.parseInt(args[0])); 
            else
                registry = LocateRegistry.getRegistry();
            registry.rebind("ChatService", s_stub);

            System.out.println("Chat ready");

        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }
    }
}