import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerImpl implements Server_interface {

    private List<Client_interface> clients;
    private File history;
    private FileWriter fw;
    BufferedReader buf;

    public ServerImpl() throws RemoteException {
        clients = new ArrayList<>();
        history = new File("history/history.txt");
        try {
            if (!history.exists()) {
                history.getParentFile().mkdirs();
                history.createNewFile();
            }
            fw = new FileWriter(history, true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to initialize server: " + e.getMessage());
        }
    }

    @Override
    public void joinChat(Client_interface client) throws RemoteException, IOException {
        // ask the client for a name
        String clientName = client.getName();
        // check if name is unique
        if (isClientNameTaken(clientName)) {
            return; // case name is already taken
        }
        
        clients.add(client);
        // inform users of a new member in the chat
        sendAll(client.getName() + " has joined the chat.\n");
        FileReader fr = new FileReader(history);
        BufferedReader buf=new BufferedReader(fr);
        String line = buf.readLine();
        // load history of the chat
        while(line!=null){
            client.receive(line);
            line = buf.readLine();
        }
        fr.close();
        buf.close();
        client.receive("Welcome to the chat, " + clientName + "! Type '/exit' to quit at anytime");
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
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        String formatedMessage = "[" + formattedTime + "] "+ message;
        for (Client_interface client : clients) {
            try {
                client.receive(formatedMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        // add the new message to the history file
        try {
            fw.write(formatedMessage + '\n');
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClientNameTaken(String name) throws RemoteException {
        for (Client_interface client : clients) {
            try {
                if (client.getName().equalsIgnoreCase(name)) {
                    return true; // Name is already taken
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false; // Name is not taken
    }

    

    public void close() {
        try {
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}