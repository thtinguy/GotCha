package sweng.epfl.ch.gotcha;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongchingyiii on 26/11/15.
 */
public class Wish {

    private Long wishId;
    private String keyword;

    public Wish(Long wishId, String keyword)
    {
        this.wishId=wishId;
        this.keyword=keyword;
    }
    public Long getWishId(){
        return wishId;
    }
    public String getKeyword(){
        return keyword;
    }
   public static Wish fromJSON (JSONObject json) throws JSONException {
       String keyword = json.getString("keyword");
       Long wishid= json.getLong("wishId");
       return new Wish(wishid,keyword);
    }

}
