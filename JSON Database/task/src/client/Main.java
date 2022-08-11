package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import helpers.FileHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    final String cIP = "127.0.0.1";
    final int portNr = 31337;
    @Parameter(names = "-t")
    String type = null;
    @Parameter(names = "-k")
    String key = null;
    @Parameter(names = "-v")
    String value = null;

    @Parameter(names = "-in")
    String inFile = null;
    private final String clientPath = System.getProperty("user.dir") + "\\src\\client\\data\\";

    Map<String, String> request;

    public static void main(String[] args) {

        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);

        main.run();
    }

    void run() {
        try (Socket socket = new Socket(InetAddress.getByName(cIP), portNr)) {
            //send logic
            System.out.println("Client started!");

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            request = new LinkedHashMap<>();
            forgeRequest(type, key, value);
            String cmd = new Gson().toJson(request);

            dataOutputStream.writeUTF(cmd);
            System.out.println("Sent: " + cmd);

            String response = inputStream.readUTF();
            System.out.println("Received: " + response + "\n\n");

        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    void forgeRequest(String type, String key, String value) throws IOException {

        if (inFile != null) {
            FileHandler fileHandler = new FileHandler(clientPath + inFile);
            new Gson().fromJson(fileHandler.readJsonFromFile(), Main.class);
        }
        request.put("type", type);
        if (key != null) {
            request.put("key", key);
        }
        if (value != null) {
            request.put("value", value);
        }
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Key: %s, Value: %s", this.type, this.key, this.value);
    }
}
