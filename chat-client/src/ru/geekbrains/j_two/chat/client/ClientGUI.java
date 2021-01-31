package ru.geekbrains.j_two.chat.client;

import ru.geekbrains.j_two.chat.library.Protocol;
import ru.geekbrains.j_two.network.SocketThread;
import ru.geekbrains.j_two.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String WINDOW_TITLE = "Chat client";
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("[HH:mm:ss] ");

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2,3));
    private final JPanel panelRight = new JPanel(new BorderLayout());
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always On Top");
    private final JTextField tfLogin = new JTextField("donspb");
    private final JPasswordField tfPassword = new JPasswordField("donspb");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private final JButton btnRename = new JButton("RenaME");

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private JScrollPane scrollLog;
    private JScrollPane scrollUsers;

    private SocketThread socketThread;

    private final JList<String> userList = new JList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        log.setEditable(false);
        scrollLog = new JScrollPane(log);
        scrollUsers = new JScrollPane(userList);
        scrollUsers.setPreferredSize(new Dimension(100,0));
        btnRename.setPreferredSize(new Dimension(100,30));
        cbAlwaysOnTop.addActionListener(this);

        btnSend.addActionListener(this);
        tfMessage.addActionListener(this);
        btnLogin.addActionListener(this);
        btnDisconnect.addActionListener(this);
        btnRename.addActionListener(this);


        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        panelRight.add(scrollUsers, BorderLayout.CENTER);
        panelRight.add(btnRename, BorderLayout.SOUTH);

        add(scrollLog, BorderLayout.CENTER);
        add(panelRight, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        showLogin(true);
        setVisible(true);
    }



    private void showLogin(boolean showLogin) {
        panelTop.setVisible(showLogin);
        panelRight.setVisible(!showLogin);
        scrollLog.setVisible(!showLogin);
        panelBottom.setVisible(!showLogin);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        }
        else if (src == btnLogin) {
            String oldLines = null;
            try {
                oldLines = LogFilesControl.getSomeLines(tfLogin.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (oldLines != null ) log.append(oldLines);

            connect();
        }
        else if (src == btnSend || src == tfMessage) {
            sendMessage();
        }
        else if (src == btnDisconnect) {
            socketThread.close();
        }
        else if (src == btnRename) {
            String newNickname = JOptionPane.showInputDialog(this, "Enter new nickname: ");
            socketThread.sendMessage(Protocol.getUserChangename(newNickname));
        }
        else throw new RuntimeException("Undefined source: " + src);
    }

    private  void connect() {
        try {
            Socket s = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this, "Client", s, executorService);
        } catch (IOException e) {
            e.printStackTrace();
            uncaughtException(Thread.currentThread(), e);
        }
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
//        String username = tfLogin.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.grabFocus();
        socketThread.sendMessage(Protocol.getUserBroadcast(msg));
//        saveLog();
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
                LogFilesControl.writeLogFile(tfLogin.getText(), msg);
            }
        });
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        StackTraceElement[] ste = e.getStackTrace();
        String msg = String.format("Exception in thread \"%s\" %s: %s\n\tat %s", t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }


    /**
     * Socket Thread Listener implementation
     *
     */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putLog("Socket Stopped");
        showLogin(true);
        setTitle(WINDOW_TITLE);
        userList.setListData(new String[0]);
        executorService.shutdown();
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");
        // showLogin(false);
        socketThread.sendMessage(Protocol.getAuthRequest(tfLogin.getText(), new String(tfPassword.getPassword())));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    private void handleMessage(String msg) {
        String[] arr = msg.split(Protocol.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Protocol.AUTH_ACCEPT:
                setTitle(WINDOW_TITLE + " nickname: " + arr[1]);
                showLogin(false);
                break;
            case Protocol.AUTH_DENIED:
                putLog("Authorization Failed");
                break;
            case Protocol.MSG_FORMAT_ERROR:
                putLog(msg);
                socketThread.close();
                break;
            case Protocol.TYPE_BROADCAST:
                putLog(String.format("%s%s: %s", DATE_FORMAT.format(Long.parseLong(arr[1])), arr[2], arr[3]));
                break;
            case Protocol.USER_LIST:
                String users = msg.substring(Protocol.USER_LIST.length() + Protocol.DELIMITER.length());
                String[] usersArr = users.split(Protocol.DELIMITER);
                Arrays.sort(usersArr);
                userList.setListData(usersArr);
                break;
            case Protocol.USER_RENAMED:
                putLog(arr[1] + " becomes " + arr[2]);
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
        }
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }
}