package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;


@Entity
public class Wish extends Model implements Persistable, Servable {
    
    /*
     * Initiates a Finder object which is then used to query the database and retrieve item objects
     */
    public static Finder<Long, Wish> find = new Finder<Long, Wish>(Wish.class);
    
    @Id
    public Long wishId;
    @CreatedTimestamp
    public Timestamp created;
    public String keyword;
    public Long ownerId;
    
    public Wish(String keyword, Long ownerId) {
        if(keyword == null) {
            throw new IllegalArgumentException("Empty keyword field");
        }
        if(ownerId == null) {
            throw new IllegalArgumentException("Empty ownerId field");
        }
        
        this.keyword = keyword;
        this.ownerId = ownerId; 
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
        Wish.deleteById(this.wishId);
    }
    
    /*
     * Returns the list of all Items currently stored in the Database
     */
    public static List<Wish> all() {
        return Wish.find.all();
    }
    
    /*
     * Returns the Item instance that has the Id given 
     * as argument.
     * 
     * @params id the id of the Item we are looking to retrieve 
     */
    public static Wish getById(Long id) {
        return Wish.find.byId(id);
    }
    /*
     * Deletes the Item instance that has the Id given
     * as argument.
     */
    public static void deleteById(Long id) {
        Wish.find.ref(id).delete();
    }
    
    /*
     * (non-Javadoc)
     * @see models.Servable#responsify()
     */
    public JsonResponse responsify() {
        return new WishJsonResponse(this);
    }

    public static List<JsonResponse> responsifyList(List<Wish> list) {
        List<JsonResponse> result = new ArrayList<>();
       
        for(Wish wish : list) {
            result.add(wish.responsify());
        }
        
        return result;
    }

}
