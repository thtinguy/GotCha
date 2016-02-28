package sweng.epfl.ch.gotcha;


import org.json.JSONException;
import org.json.JSONObject;

public class Item {


    private Long itemId;
    private String name;
    private String description;
    private Category category;
    private int price;
    private long ownerId;
    private String image;
    public Item(Long itemId, String name, String description, Category category, int price,
                long ownerId, String image) {
        this.image = image;
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.ownerId = ownerId;
        this.image = image;
    }

    public static Item fromJSON(JSONObject jsonObject) throws JSONException {
        // Check that Strings are correct.
        Category category;
        String categoryString = jsonObject.getString("category");

        switch (categoryString) {
            case "BOOK":
                category = Category.BOOK;
                break;
            case "CLOTHING":
                category = Category.CLOTHING;
                break;
            case "HOUSE":
                category = Category.HOUSE;
                break;
            case "ELECTRONIC":
                category = Category.ELECTRONIC;
                break;
            case "SPORT":
                category = Category.SPORT;
                break;
            case "MYITEMS":
                category=Category.MYITEMS;
                break;
            case "WISHLIST":
                category=Category.WISHLIST;
                break;
            default:
                category = Category.OTHER;
        }


        try {
            return new Item(
                    jsonObject.getLong("itemId"),
                    jsonObject.getString("name"),
                    jsonObject.getString("description"),
                    category,
                    jsonObject.getInt("price"),
                    jsonObject.getLong("ownerId"),
                    jsonObject.getString("image"));

        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid structure");
        } catch (NullPointerException e) {
            throw new JSONException("Invalid structure");
        }
    }

    public Long getId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getOwner() {
        return ownerId;
    }

    public String getImage() {
        return image;
    }


    public enum Category {BOOK, CLOTHING, HOUSE, ELECTRONIC, SPORT, OTHER, MYITEMS, SEARCH, WISHLIST}


}
