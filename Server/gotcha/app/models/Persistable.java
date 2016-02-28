package models;


/*
 * Interface describing an object that can be saved
 * to the application Database.
 */
public interface Persistable {

    /*
     * This method persists the object into the Database
     */
    public void saveToDB();
    /*
     * This method deletes the object from the Database
     */
    public void deleteFromDB();
    
}
