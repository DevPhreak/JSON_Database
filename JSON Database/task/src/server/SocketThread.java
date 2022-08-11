package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class SocketThread extends Thread {
    private Socket socket;
    private final ServerSocket serverSocket;
    private final Main main;

    public SocketThread(ServerSocket serverSocket, Main main) {
        this.serverSocket = serverSocket;
        this.main = main;
    }

    @Override
    public void run() {
        String dbFile = System.getProperty("user.dir") + "/src/server/data/db.json";
        try {
            socket = serverSocket.accept();
            Database db = new Database(dbFile);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Command inputCommand = new Gson().fromJson(dis.readUTF(), Command.class);
            Map<String, String> response = new LinkedHashMap<>();

            String key = inputCommand.getKey();
            String type = inputCommand.getType();
            String value = inputCommand.getValue();

            switch (type) {
                case "get":
                    String result = db.getData(key);
                    if (result.equals("No such key")) {
                        response.put("response", "ERROR");
                        response.put("reason", result);
                    } else {
                        response.put("response", "OK");
                        response.put("value", result);
                    }
                    break;

                case "set":
                    if (db.setData(key, value)) {
                        response.put("response", "OK");
                    } else {
                        response.put("response", "Something went horribly wrong");
                    }
                    break;

                case "delete":
                    if (db.deleteData(key)) {
                        response.put("response", "OK");
                    } else {
                        response.put("response", "ERROR");
                        response.put("reason", "No such key");
                    }
                    break;
                case "exit":
                    response.put("response", "OK");
                    dos.writeUTF(new Gson().toJson(response));
                    main.keepRunning = false;
                    socket.close();
                    serverSocket.close();
                    return;
            }
            dos.writeUTF(new Gson().toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
