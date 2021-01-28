package ru.geekbrains.j_two.chat.library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Protocol {

    //common data
    // ±
    //client-to-server
    // /auth_request±login±password
    // /user_bcast±msg
    // /user_changename±newname

    //server-to-client(s)
    // /auth_accept±nickname
    // /auth_denied
    // /broadcast±src±msg
    // /msg_format_error
    // /user_list±u1±u2±u3±....
    // /user_renamed±oldnick±newnick


    public static final String DELIMITER = "±";
    public static final String AUTH_REQUEST = "/auth_request";
    public static final String AUTH_ACCEPT = "/auth_accept";
    public static final String AUTH_DENIED = "/auth_denied";
    public static final String MSG_FORMAT_ERROR = "/msg_format_error";
    public static final String TYPE_BROADCAST = "/bcast";
    public static final String USER_LIST = "/user_list";
    public static final String USER_BROADCAST = "/user_bcast";
    public static final String USER_CHANGENAME = "/user_chname";
    public static final String USER_RENAMED = "/user_renamed";

    public static String getUserRenamed(String oldname, String newname) {
        return USER_RENAMED + DELIMITER + oldname + DELIMITER + newname;
    }

    public static String getUserChangename(String nickname) {
        return USER_CHANGENAME + DELIMITER + nickname;
    }

    public static String getUserBroadcast(String msg) {
        return USER_BROADCAST + DELIMITER + msg;
    }

    public static String getUserList(String users) {
        return USER_LIST + DELIMITER + users;
    }

    public static String getAuthRequest(String login, String password) {
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }

    public static String getAuthAccept(String nickname) {
        return AUTH_ACCEPT + DELIMITER + nickname;
    }

    public static String getAuthDenied() {
        return AUTH_DENIED;
    }

    public static String getMsgFormatError(String message) {
        return MSG_FORMAT_ERROR + DELIMITER + message;
    }

    public static String getTypeBroadcast(String src, String message) {
        return TYPE_BROADCAST + DELIMITER + System.currentTimeMillis() + DELIMITER + src + DELIMITER + message;
    }
}