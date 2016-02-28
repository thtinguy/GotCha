package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

/*
 * Message which is sent by a user to another when
 * he would like to buy an Item displayed by someone on the App.
 */
@Entity
public class Message extends Model implements Persistable, Servable {

    /*
     * Initiates a Finder object which is then used to query the database and retrieve User objects
     */
    public static Finder<Long, Message> find = new Finder<Long, Message>(Message.class);
    
    @Id
    public Long messageId;
    @CreatedTimestamp
    public Timestamp created;
    public Long senderId;
    public Long receiverId;
    public Long itemId;
    public String text;
    public boolean opened;
    
    /*
     * Creates a new message instance with the provided arguments
     * 
     * @param senderId the id of the User sending the message
     * @param receiverId the id of the User to which the message is being sent
     * @param itemId the id of the Item that is being referenced in the Message
     * @param text the body of the message, containing the User inputed text
     * 
     * @throws IllegalArgumentException if the message body is blank
     */
    public Message(Long senderId, Long receiverId, Long itemId, String text) {
        if (text == null)  {
            throw new IllegalArgumentException("Blank message text");
        }
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.itemId = itemId;
        this.text = text;
        this.opened = false;
    }
    
    /*
     * (non-Javadoc)
     * @see models.Persistable#saveToDB()
     */
    public void saveToDB() {
        this.save();
    }
    
    /*
     * (non-Javadoc)
     * @see models.Persistable#deleteFromDB()
     */
    public void deleteFromDB() {
        Message.deleteById(this.messageId);
    }
    
    /*
     * Returns the list of all Messages currently stored in the Database
     */
    public static List<Message> all() {
        return Message.find.all();
    }
    
    /*
     * Returns the Message instance that has the Id given 
     * as argument.
     * 
     * @param id the id of the Message we are looking to retrieve 
     */
    public static Message getById(Long id) {
        return Message.find.byId(id);
    }
    /*
     * Deletes the Message instance that has the Id given
     * as argument.
     */
    public static void deleteById(Long id) {
        Message.find.ref(id).delete();
    }
    
    /*
     * (non-Javadoc)
     * @see models.Servable#responsify()
     */
    public JsonResponse responsify() {
        return new MessageJsonResponse(this);
    }
    
    /*
     * Transforms a list of Messages into a list of message response objects
     */
    public static List<JsonResponse> responsifyList(List<Message> list) {
        List<JsonResponse> result = new ArrayList<>();
       
        for(Message message : list) {
            result.add(message.responsify());
        }

        return result;
    }

    
    
}
