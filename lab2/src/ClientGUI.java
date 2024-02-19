import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        super("Chat Client");

        this.server = server;
        this.clientName = clientName;

        initComponents();

        try {
            this.client = new ClientImpl(clientName, this);
            server.joinChat(client);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to join chat: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setVisible(true);

        // leave chat detection if the client closes the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    server.leaveChat(client);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true); // allows line wrapping
        chatArea.setWrapStyleWord(true); // line wrapping doesn't cut words
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

        // Size of the GUI
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                if (message.equals("/exit")){
                    System.exit(0);
                } else {
                    server.send(message, clientName);
                    messageField.setText("");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to send message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void receiveMessage(String message) {
        if (chatArea != null) {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        } else {
            System.err.println("ChatArea is null. Message not appended: " + message);
        }
    }
    

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java ClientGUI <rmiregistry host> <rmiregistry port>");
                return;
            }

            String host = args[0];
            int port = Integer.parseInt(args[1]);

            Registry registry = LocateRegistry.getRegistry(host, port);
            Server_interface server = (Server_interface) registry.lookup("ChatService");

            String clientName;
            do {
                clientName = JOptionPane.showInputDialog(null, "Enter your name:");
                if (clientName == null) {
                    System.exit(0);
                }

                while (clientName.trim().isEmpty() || server.isClientNameTaken(clientName)) {
                    if (clientName.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid name. Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (server.isClientNameTaken(clientName)) {
                        JOptionPane.showMessageDialog(null, "The name '" + clientName + "' is already taken. Please choose a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    clientName = JOptionPane.showInputDialog(null, "Enter your name:");
                    if (clientName == null) {
                        System.exit(0);
                    }
                }
            } while (clientName == null || clientName.trim().isEmpty() || server.isClientNameTaken(clientName));

            ClientGUI cg = new ClientGUI(server, clientName);
            Client_interface c = new ClientImpl(clientName, cg);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void scrollToBottom() {
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
    
}
