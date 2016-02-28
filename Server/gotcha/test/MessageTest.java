import static org.junit.Assert.*;
import models.Message;

import org.junit.Test;


public class MessageTest {

    /*
     * Creates a Message entity, and makes sure all fields 
     * are correctly initialized by the constructor
     */
    @Test
    public void constructorWorks() {
        Long senderId = 123L;
        Long receiverId = 456L;
        Long itemId = 789L;
        String text = "Hey, can you give me a better price on that book";

        Message message = new Message(senderId, receiverId, itemId, text);
        
        
        assertEquals(senderId, message.senderId);
        assertEquals(receiverId, message.receiverId);
        assertEquals(itemId, message.itemId);
        assertEquals(text, message.text);
        assertFalse(message.opened);
       
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a null message text
     */
    @Test(expected= IllegalArgumentException.class) 
    public void constructorThrowsExceptionOnInvalidText() {
        Long senderId = 123L;
        Long receiverId = 456L;
        Long itemId = 789L;
        String text = null;

        Message message = new Message(senderId, receiverId, itemId, text); 
    }

}
