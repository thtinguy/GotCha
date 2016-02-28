package models;

import java.sql.Timestamp;


public class WishJsonResponse extends JsonResponse {

    public Long wishId;
    public Timestamp created;
    public String keyword;
    public Long ownerId;
    
    public WishJsonResponse(Long wishId, Timestamp created, String keyword, Long ownerId) {
        this.wishId = wishId;
        this.created = created;
        this.keyword = keyword;
        this.ownerId = ownerId;
    }
    
    public WishJsonResponse(Wish wish) {
        this(wish.wishId, wish.created, wish.keyword, wish.ownerId);
    }
}
