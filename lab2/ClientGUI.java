import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientGUI extends JFrame {
    private Server_interface server;
    private Client_interface client;
    private JTextArea chatArea;
    private JTextField messageField;
    private String clientName;

    public ClientGUI(Server_interface server, String clientName) {
        super(clientName + "'s Chat");

        this.server = server;
        this.clientName = clientName;

        try {
            client = new ClientImpl(clientName, this);
            try {
                server.joinChat(client);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to join chat: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                server.send(message, clientName);
                messageField.setText("");
            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to send message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void receiveMessage(String message) {
        if (chatArea != null) {
            chatArea.append(message + "\n");
        } else {
            System.err.println("ChatArea is null. Message not appended: " + message);
        }
    }
    

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java ClientGUI <rmiregistry host> <rmiregistry port> <clientName>");
            return;
        }

        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String clientName = args[2];

            Registry registry = LocateRegistry.getRegistry(host, port);
            Server_interface server = (Server_interface) registry.lookup("ChatService");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ClientGUI(server, clientName);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
