package models;

import java.sql.Timestamp;


/*
 * Response object of the Message class that will be parsed to JSON
 */
public class MessageJsonResponse extends JsonResponse {
    
    public Long messageId;
    public Timestamp created;
    public Long senderId;
    public Long receiverId;
    public Long itemId;
    public String text;
    public boolean opened;
    
    /*
     * Creates an instance of message response using provided values
     */
    public MessageJsonResponse(Long messageId, Timestamp created, Long senderId, Long receiverId, Long itemId, String text, boolean opened){
        this.messageId = messageId;
        this.created = created;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.itemId = itemId;
        this.text = text;
        this.opened = opened;
    }
    
    /*
     * Creates an instance of message response using an actual message object
     */
    public MessageJsonResponse(Message message) {
        // Assume checking of argumenst is done in the constructor
        this(message.messageId, message.created, message.senderId, message.receiverId, message.itemId, message.text, message.opened);
    }
}
