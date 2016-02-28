import models.Item;
import models.Message;
import models.User;
import models.Item.Category;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.junit.Assert.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    /**
     * In this example we just check if the welcome page is being shown
     * correctly.
     */
    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertTrue(browser.pageSource().contains("Gotcha"));
            }
        });
    }
    
    
    /*
     * Integration test making sure all methods that manipulate 
     * Database Item entities are working as expected.
     */
    @Test
    public void itemClassDbManipsWork() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                String name = "Algorithm Book";
                String description = "Brand new book";
                Category category = Category.BOOK;
                int price =  35;
                Long userId = 1234L;
                
                Item persistedItem = new Item(name, description, category, price, userId);
                Item nonPersistedItem = new Item(name, description, category, price, userId);
                Item deletedItem = new Item(name, description, category, price, userId);
                
                
                persistedItem.saveToDB();
                deletedItem.saveToDB();
                
                Item.deleteById(deletedItem.itemId);
                
                assertFalse(persistedItem.itemId == null);
                assertTrue(nonPersistedItem.itemId == null);
                assertTrue(Item.all().contains(persistedItem));
                assertTrue(Item.getById(persistedItem.itemId).equals(persistedItem));
                assertFalse(Item.all().contains(deletedItem));
            }
        });
    }
    
    /*
     * Integration test making sure all methods that manipulate 
     * Database User entities are working as expected.
     */
    @Test
    public void userClassDbManipsWork() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                String firstName = "Henry";
                String lastName = "Ford";
                String email = "henry@fordmotors.com";
                
                User persistedUser = new User(firstName, lastName, email);
                User nonPersistedUser = new User(firstName, lastName, email);
                User deletedUser = new User(firstName, lastName, email);
                
                persistedUser.saveToDB();
                deletedUser.saveToDB();
                
                User.deleteById(deletedUser.userId);
                
                assertFalse(persistedUser.userId == null);
                assertTrue(nonPersistedUser.userId == null);
                assertTrue(User.all().contains(persistedUser));
                assertTrue(User.getById(persistedUser.userId).equals(persistedUser));
                assertFalse(User.all().contains(deletedUser));
            }
        });
    }
    
    /*
     * Integration test making sure all methods that manipulate 
     * Database User entities are working as expected.
     */
    @Test
    public void messageClassDbManipsWork() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                Long senderId = 123L;
                Long receiverId = 456L;
                Long itemId = 789L;
                String text = "Hey, can you give me a better price on that book";

                Message persistedMessage = new Message(senderId, receiverId, itemId, text);
                Message nonPersistedMessage = new Message(senderId, receiverId, itemId, text);
                Message deletedMessage = new Message(senderId, receiverId, itemId, text);
                
                persistedMessage.saveToDB();
                deletedMessage.saveToDB();
                
                Message.deleteById(deletedMessage.messageId);
                
                assertFalse(persistedMessage.messageId == null);
                assertTrue(nonPersistedMessage.messageId == null);
                assertTrue(Message.all().contains(persistedMessage));
                assertFalse(Message.all().contains(deletedMessage));
                assertTrue(Message.getById(persistedMessage.messageId).equals(persistedMessage));
            }
        });
    }

}
