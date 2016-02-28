package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

/*
 * Users of the platform, with basic information.
 */
@Entity
@Table(name="users")
public class User extends Model implements Persistable, Servable {
    
    /*
     * Initiates a Finder object which is then used to query the database and retrieve User objects
     */
    public static Finder<Long, User> find = new Finder<Long, User>(User.class);
    
    @Id
    public Long userId;
    @CreatedTimestamp
    public Timestamp created;
    public String firstName;
    public String lastName;
    public String email;
    
    /*
     * Creates a new User instance with given arguments
     * 
     * @param firstName the first name of the user
     * @param lastName the last name of the user 
     * @param email the email of the user
     */
    public User(String firstName, String lastName, String email) {
        if (firstName == null) {
            throw new NullPointerException("User first name is null");
        }
        if (lastName == null) {
            throw new NullPointerException("User last name is null");
        }
        if (email == null) {
            throw new NullPointerException("User email is null");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
        User.deleteById(this.userId);
    }
    
    /*
     * Returns the list of all Users currently stored in the Database
     */
    public static List<User> all() {
        return User.find.all();
    }
    
    /*
     * Returns the User instance that has the Id given 
     * as argument.
     * 
     * @param id the id of the User we are looking to retrieve 
     */
    public static User getById(Long id) {
        return User.find.byId(id);
    }
    /*
     * Deletes the User instance that has the Id given
     * as argument.
     */
    public static void deleteById(Long id) {
        User.find.ref(id).delete();
    }
    
    /*
     * (non-Javadoc)
     * @see models.Servable#responsify()
     */
    public JsonResponse responsify() {
        return new UserJsonResponse(this);
    }
    
    public static List<JsonResponse> responsifyList(List<User> users) {
        List<JsonResponse> result = new ArrayList<>();
        
        for(User user : users) {
            result.add(user.responsify());
        }
        
        return result;
    }

}
