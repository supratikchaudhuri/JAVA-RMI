import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client class that is used to interact with a server and invoke remote methods
 */
public class Client {
  static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

  /**
   * Driver method of this class
   * @param args accepts command line arguments
   */
  public static void main (String[] args) {
    try {
      if (args.length != 2 || Integer.parseInt(args[1]) > 65535) {
        throw new IllegalArgumentException("Invalid arguments. " +
                "Please provide valid IP and PORT (0-65535) number and start again.");
      }

      System.setProperty("sun.rmi.transport.tcp.responseTimeout", "30000");
      System.setProperty("sun.rmi.transport.tcp.connectionTimeout", "30000");

      Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
      RemoteMethodInterface rmi = (RemoteMethodInterface) reg.lookup("MapMethods");
      System.out.println(getTimeStamp() + " Client started");

      label:
      while (true) {
        boolean flag = true;
        String requestTime = "";
        String request = "";
        String response = "";
        System.out.print("Operation List: \n1. Put\n2. Get\n3. Delete\n4. Exit\nChoose operation: ");
        String op = br.readLine().trim();

        switch (op) {
          case "1": {
            String key = getKey();
            String value = getValue();
            request = "PUT " + key + " | " + value;
            requestTime = getTimeStamp();
            response = rmi.addToMap(key, value);

            break;
          }
          case "2": {
            String key = getKey();
            request = "Get " + key;
            requestTime = getTimeStamp();
            response = rmi.getFromMap(key);

            break;
          }
          case "3": {
            String key = getKey();
            request = "Delete " + key;
            requestTime = getTimeStamp();
            response = rmi.deleteFroMap(key);

            break;
          }
          case "4":
            break label;
          default:
            System.out.println("Please choose a valid operation.");
            flag = false;
            break;
        }

        if(flag) {
          System.out.println(requestTime + "REQUEST: " + request);
          System.out.println(getTimeStamp() + "RESPONSE: " + response);
        }
      }

    } catch (Exception e) {
      System.out.println("ERROR: " + e);
    }
  }

  private static String getTimeStamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    return "[Time: " + sdf.format(new Date()) + "] ";
  }

  private static String getKey() throws IOException {
    System.out.print("Enter key: ");
    return br.readLine().trim();
  }

  private static String getValue() throws IOException {
    System.out.print("Enter Value: ");
    return br.readLine().trim();
  }
}
