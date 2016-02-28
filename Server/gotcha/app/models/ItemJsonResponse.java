package models;

import models.Item.Category;

/*
 * Response object of the Item class that will be parsed to JSON
 */
public class ItemJsonResponse extends JsonResponse {
    
    public final Long itemId;
    public final Long ownerId;
    public final String name;
    public final String description;
    public final Category category;
    public final int price;
    public final String image;
    
    /*
     * Creates an instance of an Item response using provided arguments
     * 
     * @param itemId the id of the item 
     * @param ownderId the id of the owner of the item
     * @param name the name of the item
     * @param description the description of the item
     * @param category the category to which the item belongs
     * @param price the price of the item
     * 
     */
    public ItemJsonResponse(Long itemId, Long ownerId, String name, String description, Category category, int price, String image) {
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
    }
    
    /*
     * Creates an instance of an Item response using an Item provided as argument
     * 
     * @param item an Item to be converted to a response 
     */
    public ItemJsonResponse(Item item) {
        // Assume checking of values is done in the Item constructor
        this(item.itemId, item.ownerId, item.name, item.description, item.category, item.price, item.imageBase64);
    }
}
