package sweng.epfl.ch.gotcha;


import java.util.List;

/**
 * A client object to the server that abstracts the underlying
 * communication protocol and data formats.
 */

public interface GotchaClient {

    void postItem(String item) throws Exception;

    int postMessage(String message) throws Exception;

    Item getItemById(Long id) throws Exception;

    List<Item> getItemsByCat(Item.Category category) throws Exception;

    List<Message> getMessages(Long receiverId) throws Exception;

    String getUserNameById(Long userId) throws Exception;


}
