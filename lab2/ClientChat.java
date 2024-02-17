import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientChat {
    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println("Usage: java HelloClient <rmiregistry host> <rmiregistry port>");
                return;
            }
            System.out.println("cc");
            String host = args[0];
            String clientName= args[2];
            int port = Integer.parseInt(args[1]);

            Registry registry = LocateRegistry.getRegistry(host, port);

            Server_interface s = (Server_interface) registry.lookup("ChatService");

            ClientGUI clientGUI = new ClientGUI(s, clientName);

            Client_interface c = new ClientImpl(clientName, clientGUI);

            s.joinChat(c);
            
            try (Scanner scanner = new Scanner(System.in)) {
                String message;
                while (true) {
                    System.out.print("\n"+clientName +": ");
                    message = scanner.nextLine();
                    
                    // Move cursor up one line
                    System.out.print("\033[1A");
                    
                    // Clear the line
                    System.out.print("\033[2K");

                    // Check for exit command
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    
                    s.send(message, clientName);
                    System.out.println("\n");
                }
            }
            s.leaveChat(c);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
