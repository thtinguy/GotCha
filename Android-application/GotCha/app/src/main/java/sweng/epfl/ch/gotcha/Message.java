package sweng.epfl.ch.gotcha;


import org.json.JSONException;
import org.json.JSONObject;

public class Message {


    private String message;
    private long   senderId;
    private long   receiverId;
    private long   itemId;
    private long messageId;
    private boolean opened;

    public Message(String message, long senderId, long receiverId, long itemId, long messageId, boolean opened){

        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.itemId=itemId;
        this.messageId = messageId;
        this.opened = opened;
    }

    public String getMessage(){ return message; }
    public long getSenderId(){
        return senderId;
    }
    public long getReceiverId(){
        return receiverId;
    }
    public long getItemId() { return itemId; }
    public long getMessageId() { return messageId; }
    public boolean isOpened() { return opened; }

    public JSONObject toJson() {

        JSONObject json = new JSONObject();

        try {
            json.put("text", message);
            json.put("senderId", senderId);
            json.put("receiverId", receiverId);
            json.put("itemId", itemId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static Message fromJSON(JSONObject jsonObject) throws JSONException {
        // Check that Strings are correct.

        try {
            return new Message(
                    jsonObject.getString("text"),
                    jsonObject.getLong("senderId"),
                    jsonObject.getLong("receiverId"),
                    jsonObject.getLong("itemId"),
                    jsonObject.getLong("messageId"),
                    jsonObject.getBoolean("opened"));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid structure");
        } catch (NullPointerException e) {
            throw new JSONException("Invalid structure");
        }
    }

}
