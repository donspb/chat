package ru.geekbrains.j_two.chat.server.core;

import ru.geekbrains.j_two.chat.library.Protocol;
import ru.geekbrains.j_two.network.SocketThread;
import ru.geekbrains.j_two.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {

    private String nickname;
    private boolean isAuthorized;

    public boolean isReconnecting() {
        return isReconnecting;
    }

    private boolean isReconnecting;

    public void reconnect() {
        isReconnecting = true;
        close();
    }

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Protocol.getAuthAccept(nickname));
    }

    void renameDone(String nickname) {
        this.nickname = nickname;
    }

    void authFail() {
        sendMessage(Protocol.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(Protocol.getMsgFormatError(msg));
        close();
    }

}
