import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerImpl extends UnicastRemoteObject implements Server_interface {

    private List<Client_interface> clients;
    private static final String HISTORY_FILE = "history/history.txt";

    public ServerImpl() throws RemoteException {
        clients = new ArrayList<>();
    }

    @Override
    public void joinChat(Client_interface client) throws RemoteException, IOException {
        clients.add(client);

        // Read the chat history
        List<String> historyMessages = readChatHistory();

        // Send the chat history to the client GUI
        for (String message : historyMessages) {
            client.receive(message);
        }

        // Inform other clients that the new client has joined
        sendAll(client.getName() + " has joined the chat.\n");
    }

    @Override
    public void leaveChat(Client_interface client) throws RemoteException {
        clients.remove(client);
        sendAll(client.getName() + " has left the chat.\n");
    }

    @Override
    public void send(String message, String sender) throws RemoteException {
        sendAll(sender + " : " + message);
    }

    @Override
    public void sendAll(String message) throws RemoteException {
        for (Client_interface client : clients) {
            try {
                client.receive(message);
            } catch (RemoteException e) {
                // Handle remote exception if the client is unreachable
                // You may want to remove the client from the list in this case
                e.printStackTrace();
            }
        }
        // Write to the history file
        // You might want to add the new message to the history file here as well
    }

    // Method to read the chat history from the file
    private List<String> readChatHistory() throws IOException {
        List<String> historyMessages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyMessages.add(line);
            }
        }
        return historyMessages;
    }
    @Override
    public List<String> requestChatHistory() throws RemoteException {
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            List<String> historyMessages = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                historyMessages.add(line);
            }
            return historyMessages;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to read chat history: " + e.getMessage());
        }
    }

}
