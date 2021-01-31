package ru.geekbrains.j_two.chat.client;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;

public class LogFilesControl {

    private static final String HISTORY_PREFIX = "history\\history_";
    private static final int START_LINES = 100;

    public static void writeLogFile(String login, String msg) {
        msg += "\n";
        Path path = Paths.get(HISTORY_PREFIX + login + ".log");
        if (Files.notExists(path)) createNewLogfile(path);

        try {
            Files.write(path, msg.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewLogfile(Path path) {
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectory(path.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSomeLines(String login) throws IOException {
        File path = new File(HISTORY_PREFIX + login + ".log");
        if (!path.exists()) return null;

        RandomAccessFile raf = new RandomAccessFile(path, "r");
        long StringPos = raf.length() - 1;
        int counter = 0;
        long bytesCounter = 0;
        raf.seek(StringPos);

        do {
            char tempch = (char) raf.read();
            if (tempch == '\n') counter++;
            StringPos--;
            raf.seek(StringPos);
            bytesCounter++;
        } while (counter <= START_LINES && StringPos >= 0);

        raf.seek(raf.length() - bytesCounter);
        StringBuilder readString = new StringBuilder();
        for (int i = 0; i < counter; i++) readString.append(raf.readLine() + "\n");
        raf.close();
        return readString.toString();
    }
}
