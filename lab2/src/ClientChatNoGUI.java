import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientChatNoGUI {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java ClientChatNoGUI <rmiregistry host> <rmiregistry port>");
                return;
            }
            
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Registry registry = LocateRegistry.getRegistry(host, port);

            Server_interface s = (Server_interface) registry.lookup("ChatService");

            Scanner scanner = new Scanner(System.in);

            String clientName;
            do {
                System.out.print("Enter your name: ");
                clientName = scanner.nextLine().trim();

                if (clientName.isEmpty()) {
                    System.out.println("Invalid name. Please enter a valid name.");
                } else if (s.isClientNameTaken(clientName)) {
                    System.out.println("The name '" + clientName + "' is already taken. Please choose a different name.");
                }
            } while (clientName.isEmpty() || s.isClientNameTaken(clientName));

            Client_interface client = new ClientImpl(clientName);
            s.joinChat(client);

            String message;
            while (true) {
                message = scanner.nextLine();
                // Move cursor up one line
                System.out.print("\033[1A");
                if (message.equalsIgnoreCase("/exit")) {
                    break;
                }
                s.send(message, clientName);
 
            }
            
            s.leaveChat(client);

            scanner.close();
            System.exit(0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
