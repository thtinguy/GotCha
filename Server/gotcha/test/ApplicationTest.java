import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    /*
     * Trivial test to make sure the testing suite is 
     * working correctly. This should produce at least one 
     * test that is passed.
     */
    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    /*
     * Tests the correct rendering of the index view into
     * an html file.
     */
    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Gotcha");
        assertEquals("text/html", contentType(html));
        assertTrue(contentAsString(html).contains("Gotcha"));
    }


}
