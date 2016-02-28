package sweng.epfl.ch.gotcha;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private static Long userId;
    private String firstName;
    private String lastName;
    private String email;


    public User(Long userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public static void setLoggedInUser(Context context){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        userId=(long)pref.getInt("UserId",0);
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public static Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        // Check that Strings are correct.
        return new User(
                jsonObject.getLong("userId"),
                jsonObject.getString("firstName"),
                jsonObject.getString("lastName"),
                jsonObject.getString("email"));
    }



}


