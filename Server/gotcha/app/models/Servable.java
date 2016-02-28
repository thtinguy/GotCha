package models;

/*
 * Qualifies all classes that can be served to a client 
 * in a JSON format.
 */
public interface Servable {
    
    /*
     * Returns a JsonResponse instance encapsulating
     * the relevant data to be provided to the client application
     */
    public JsonResponse responsify();
    
}
