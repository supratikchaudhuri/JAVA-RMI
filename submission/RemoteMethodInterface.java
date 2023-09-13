import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote Interface class which hold all methods that are available for remote invocation by different client.
 */
public interface RemoteMethodInterface extends Remote {

  /**
   * Adds a key value pair to the map
   * @param key string format key
   * @param value string format value
   * @return response; success or failure
   * @throws IOException exception interacting with the map file
   */
   String addToMap(String key, String value) throws IOException;

  /**
   * Fetches value associated to a key in the map
    * @param key whose value client wants to fetch
   * @return response; success or failure
   * @throws RemoteException exception invoking remote procedure
   * @throws InterruptedException
   */
  String getFromMap(String key) throws RemoteException, InterruptedException;

  /**
   * Deletes a key-value pair from the map
   * @param key which client requests to be deleted
   * @return response; success or failure
   * @throws IOException exception interacting with the map file
   */
  String deleteFroMap(String key) throws IOException, InterruptedException;
}
