import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HelloClient {
    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println("Usage: java HelloClient <rmiregistry host> <rmiregistry port>");
                return;
            }

            String host = args[0];
            String clientName= args[2];
            int port = Integer.parseInt(args[1]);

            // Create client objects
            Info_client infoClient = new Info_client(clientName);
            AccountingImpl accountingClient = new AccountingImpl();

            // Export client objects as stubs
            Info_itf infoStub = (Info_itf) UnicastRemoteObject.exportObject(infoClient, 0);
            Accounting_itf accountingStub = (Accounting_itf) UnicastRemoteObject.exportObject(accountingClient, 0);

            // Get registry
            Registry registry = LocateRegistry.getRegistry(host, port);

            // Lookup server services
            Hello helloService = (Hello) registry.lookup("HelloService");
            Hello2 hello2Service = (Hello2) registry.lookup("Hello2Service");
            Registry_itf registryService = (Registry_itf) registry.lookup("RegistryService");

            // Register accounting client with the server
            registryService.register(accountingStub);

            // Remote method invocation
            String res = helloService.sayHello(infoStub);
            System.out.println(res);
            String res2 = hello2Service.sayHello(accountingStub);
            System.out.println(res2);
             res2 = hello2Service.sayHello(accountingStub);
            System.out.println(res2);
             res2 = hello2Service.sayHello(accountingStub);
            System.out.println(res2);
             res2 = hello2Service.sayHello(accountingStub);
            System.out.println(res2);
             res2 = hello2Service.sayHello(accountingStub);
            System.out.println(res2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
