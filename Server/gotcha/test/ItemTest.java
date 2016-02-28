import static org.junit.Assert.*;
import models.Item;
import models.Item.Category;

import org.junit.Test;


public class ItemTest {

    /*
     * Creates an Item entity, and makes sure all fields 
     * are correctly initialized by the constructor
     */
    @Test
    public void constructorWorks() {
        String name = "Algorithm Book";
        String description = "Brand new book";
        Category category = Category.BOOK;
        int price = 35;
        Long userId = 1234L;
        
        Item item = new Item(name, description, category, price, userId);
        
        assertEquals(name, item.name);
        assertEquals(description, item.description);
        assertEquals(category, item.category);
        assertEquals(price, item.price);
        assertEquals((Long)userId, (Long)item.ownerId);
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a null name
     */
    @Test(expected= NullPointerException.class) 
    public void constructorThrowsExceptionOnInvalidName() {
        String name = null;
        String description = "Brand new book";
        Category category = Category.BOOK;
        int price = 35;
        Long userId = 1234L;
        
        Item item = new Item(name, description, category, price, userId);
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a null description
     */
    @Test(expected= NullPointerException.class) 
    public void constructorThrowsExceptionOnInvalidDescription() {
        String name = "Algorithm Book";
        String description = null;
        Category category = Category.BOOK;
        int price = 35;
        Long userId = 1234L;
        
        Item item = new Item(name, description, category, price, userId);
    }
    
    /*
     * Makes sure the constructor throws an exception
     * if it is invoked with a negative price
     */
    @Test(expected= IllegalArgumentException.class) 
    public void constructorThrowsExceptionOnNegativePrice() {
        String name = "Algorithm Book";
        String description = "Brand new book";
        Category category = Category.BOOK;
        int price = -10;
        Long userId = 1234L;
        
        Item item = new Item(name, description, category, price, userId);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
