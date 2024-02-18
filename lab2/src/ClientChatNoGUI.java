import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientChatNoGUI {
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

            // Get registry
            Registry registry = LocateRegistry.getRegistry(host, port);

            Server_interface s = (Server_interface) registry.lookup("ChatService");

            Client_interface c = new ClientImpl(clientName);

            s.joinChat(c);
            
            try (Scanner scanner = new Scanner(System.in)) {
                String message;
                while (true) {
                    // Read user input
                    message = scanner.nextLine();
                    
                    // Move cursor up one line
                    System.out.print("\033[1A");
                    
                    // Check for exit command
                    if (message.equalsIgnoreCase("exit")) {
                        break; // Exit loop and terminate client
                    }
                    
                    // Send message to server
                    s.send(message, clientName);
                }
            }
            // Leave the chat before exiting
            s.leaveChat(c);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}