package sweng.epfl.ch.gotcha;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongchingyiii on 26/11/15.
 */
public class MyMessage {

    private Item item;
    private Message message;
    private String userName;

    public MyMessage(Item item, Message message, String userName)
    {
        this.item=item;
        this.message=message;
        this.userName=userName;
    }


    public Long getItemIdInMyMessage() {return this.item.getId();}
    public String getItemNameInMyMessage() {return this.item.getName();}
    public Item getItemInMyMessage() {return this.item;}
    public Message getMessageInMyMessage() {return this.message;}
    public String getUserNameInMyMessage() {return this.userName;}


}
