package server;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {


    private final String dbFile;
    ReadWriteLock lock;
    Lock readLock;
    Lock writeLock;

    public Database(String dbFile){
        this.dbFile = dbFile;
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    boolean setData(String key, String value){
        try {
            writeLock.lock();

            System.out.println("Before reading");
            FileReader reader = new FileReader(dbFile);
            System.out.println("Reader initialized");
            Map<String, String> database = new Gson().fromJson(reader, Map.class);      //Hier hangt ie vast
            System.out.println("Read into DB");
            reader.close();

            database.put(key, value);
            System.out.println(database.size());
            FileWriter writer = new FileWriter(dbFile);
            writer.write(new Gson().toJson(dbFile));
            writer.close();

            writeLock.unlock();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean deleteData(String key){
        try {
            writeLock.lock();
            readLock.lock();

            FileReader reader = new FileReader(dbFile);
            Map<String, String> database = new Gson().fromJson(reader, Map.class);
            reader.close();

            if (database.containsKey(key)) {
                database.remove(key);
                FileWriter writer = new FileWriter(dbFile);
                writer.write(new Gson().toJson(database));
                writer.close();
                writeLock.unlock();
                readLock.unlock();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    String getData(String key){
        try {
            readLock.lock();
            FileReader reader = new FileReader(dbFile);
            Map<String, String> database = new Gson().fromJson(reader, Map.class);
            reader.close();
            readLock.unlock();

            if(database.containsKey(key)){
             return database.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No such key";
    }
}
