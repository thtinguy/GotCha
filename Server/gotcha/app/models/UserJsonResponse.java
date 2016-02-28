package models;


/*
 * Response object of the User class that will be parsed to JSON
 */
public class UserJsonResponse extends JsonResponse {
    
    public Long userId;
    public String firstName;
    public String lastName;
    public String email;
    
    /*
     * Creates an instance of a user response object given a list of arguments
     * 
     * @param userId the id of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email of the user
     * 
     * Note: Arguments not checked, but should be used only for testing
     */
    public UserJsonResponse(Long userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    /*
     * Overriden constructor, creates an instance of a response object using a User object
     * 
     * @param user the user that must be converted to a response
     * 
     * Note: Attributes of the user not checked as it is checked in the User constructor
     */
    public UserJsonResponse(User user) {
        this(user.userId, user.firstName, user.lastName, user.email);
    }

}
