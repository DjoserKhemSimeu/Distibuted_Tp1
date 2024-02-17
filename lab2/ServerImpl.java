import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

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
        FileReader fr = new FileReader(history);
        BufferedReader buf=new BufferedReader(fr);

        clients.add(client);
        sendAll(client.getName() + " has joined the chat.\n");

        String line = buf.readLine();

        while(line!=null){
            client.receive(line);
            line = buf.readLine();
        }
        fr.close();
        buf.close();
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
                e.printStackTrace();
            }
        }
        try {
            fw.write(message + '\n');
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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