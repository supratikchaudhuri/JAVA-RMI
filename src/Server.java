import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server which is used to bind remote methods to the rmi registry and allow remote invocation by the client.
 */
public class Server {
  public static void main (String[] args) {

    try {
      if (args.length != 2 || Integer.parseInt(args[1]) > 65535) {
        throw new IllegalArgumentException("Invalid arguments. " +
                "Please provide valid IP and PORT number (0-65535) and start again.");
      }

      System.setProperty("java.rmi.server.hostname", args[0]);
      Registry reg = LocateRegistry.createRegistry(Integer.parseInt(args[1]));
      System.out.println("Server started");

      RemoteMethodInterface rmi = new MapMethods();

      reg.rebind("MapMethods", rmi);
      System.out.println("Binded objects to RMI registry for remote invocation");

    } catch (Exception e) {
      System.out.println("ERROR: " + e);
    }
  }
}
