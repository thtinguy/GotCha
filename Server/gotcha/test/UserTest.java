import static org.junit.Assert.*;
import models.Item;
import models.Item.Category;
import models.User;

import org.junit.Test;


public class UserTest {

    /*
     * Creates a User entity, and makes sure all fields 
     * are correctly initialized by the constructor
     */
    @Test
    public void constructorWorks() {
        String firstName = "Henry";
        String lastName = "Ford";
        String email = "henry@fordmotors.com";
        
        User user = new User(firstName, lastName, email);
        
        assertEquals(firstName, user.firstName);
        assertEquals(lastName, user.lastName);
        assertEquals(email, user.email);
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a null first name
     */
    @Test(expected= NullPointerException.class) 
    public void constructorThrowsExceptionOnInvalidFirstName() {
        String firstName = null;
        String lastName = "Ford";
        String email = "henry@fordmotors.com";
        
        User user = new User(firstName, lastName, email);
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a null last name
     */
    @Test(expected= NullPointerException.class) 
    public void constructorThrowsExceptionOnInvalidLastName() {
        String firstName = "Henry";
        String lastName = null;
        String email = "henry@fordmotors.com";
        
        User user = new User(firstName, lastName, email);
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a null email
     */
    @Test(expected= NullPointerException.class) 
    public void constructorThrowsExceptionOnInvalidEmail() {
        String firstName = "Henry";
        String lastName = "Ford";
        String email = null;
        
        User user = new User(firstName, lastName, email);
    }

}
