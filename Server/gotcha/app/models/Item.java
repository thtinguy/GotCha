package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

/*
 *  Item that will be listed and up for sale on the Mobile Application.
 */
@Entity
public class Item extends Model implements Persistable, Servable {
    
    // Qualifies the category in which the Item is to be displayed
    public enum Category {BOOK, CLOTHING, HOUSE, SPORT, ELECTRONIC, OTHER};
    
    /*
     * Initiates a Finder object which is then used to query the database and retrieve item objects
     */
    public static Finder<Long, Item> find = new Finder<Long, Item>(Item.class);
    
    @Id
    public Long itemId;
    @CreatedTimestamp
    public Timestamp created;
    public String name;
    public String description;
    public Category category;
    public int price;
    public Long ownerId;
    @Column(columnDefinition = "TEXT")
    public String imageBase64;
    
    
    /*
     * Creates a new Item instance given the various item attributes provided as arguments
     * 
     * @param name the name of the object
     * @param description the short description of the item
     * @param category the category to which the item belongs
     * @param price the price of the item in CHF
     */
    public Item(String name, String description, Category category, int price, Long ownerId, String imageBase64) {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }
        if (description == null) {
            throw new NullPointerException("Name is null");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price is negative or null");
        }
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.ownerId = ownerId;
        this.imageBase64 = imageBase64;
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
        Item.deleteById(this.itemId);
    }
    
    /*
     * Returns the list of all Items currently stored in the Database
     */
    public static List<Item> all() {
        return Item.find.all();
    }
    
    /*
     * Returns the Item instance that has the Id given 
     * as argument.
     * 
     * @params id the id of the Item we are looking to retrieve 
     */
    public static Item getById(Long id) {
        return Item.find.byId(id);
    }
    /*
     * Deletes the Item instance that has the Id given
     * as argument.
     */
    public static void deleteById(Long id) {
        Item.find.ref(id).delete();
    }
    
    /*
     * (non-Javadoc)
     * @see models.Servable#responsify()
     */
    public JsonResponse responsify() {
        return new ItemJsonResponse(this);
    }

    public static List<JsonResponse> responsifyList(List<Item> list) {
        List<JsonResponse> result = new ArrayList<>();
        
        for(Item item : list) {
            result.add(item.responsify());
        }
        
        return result;
    }
    
}
