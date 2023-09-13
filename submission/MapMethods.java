import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation class of the `RemoteMethodInterface` class.
 */
public class MapMethods extends UnicastRemoteObject implements RemoteMethodInterface {
  static final ReadWriteLock lock = new ReentrantReadWriteLock();
  static final Properties prop = new Properties();
  static OutputStream writer;
  static InputStream reader;
  protected MapMethods() throws IOException {
    super();

    reader = new FileInputStream("map.properties");
    prop.load(reader);
    writer = new FileOutputStream("map.properties", false);
    prop.store(writer, null);

    addToMap("hello", "world");
    addToMap("MS", "Computer Science");
    addToMap("CS6650", "Building Scalable Distributed System");
    addToMap("Firstname Lastname", "John Doe");
    addToMap("BTC", "Bitcoin");
    System.out.println("Inserted 5 default key-value pairs\n");

  }

  @Override
  public String addToMap(String key, String value) throws IOException {
    lock.writeLock().lock();
    requestLog("PUT " + key + " | " + value);
    prop.setProperty(key, value);
    prop.store(writer, null);
    lock.writeLock().unlock();

    String res = "Inserted key \"" + key + "\" with value \"" + value + "\"";
    responseLog(res);
    return res;
  }

  @Override
  public String getFromMap(String key) {
    lock.readLock().lock();
    requestLog("GET " + key);
    String value = prop.getProperty(key);
    lock.readLock().unlock();

    String res = (value == null  || value.equals("~null~")?
            "No value found for key \"" + key + "\"" : "Key: \"" + key + "\" ,Value: \"" + value + "\"");
    responseLog(res);
    return res;
  }

  @Override
  public String deleteFroMap(String key) throws IOException {
    lock.writeLock().lock();
    requestLog("DELETE " + key);
    String res = "";

    if(prop.containsKey(key)) {
      addToMap(key, "~null~");
      prop.remove(key);
      prop.store(writer, null);
      res = "Deleted key \"" + key + "\"";
    }
    else {
      res = "Key not found.";
    }
    responseLog(res);
    lock.writeLock().unlock();

    return res;
  }

  private static String getTimeStamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    return "[Time: " + sdf.format(new Date()) + "] ";
  }

  private static void requestLog(String req) {
    System.out.println(getTimeStamp() + "REQUEST: " + req);
  }

  private static void responseLog(String res) {
    System.out.println(getTimeStamp() + "RESPONSE: " + res);
  }
}


