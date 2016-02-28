package sweng.epfl.ch.gotcha;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by nguyenthaitinh on 11/11/15.
 */

// Test methods in NetworkGotchaClient class
public class NetworkGotchaClientTest implements Test {

    // Test NetworkGotchaClient 's methods



    // Test method postData
    @Test
    public void postItemTest() throws Exception {
        String item = "name=TinhTest&description=HUHUUHUHUU&price=199&userId=1&category=SPORT";

        NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());
        networkGotchaClient.postItem(item);
        List<Item> itemList = networkGotchaClient.getItemsByCat(Item.Category.SPORT);

        int index = 0 ;
        for (int i = 0; i < itemList.size(); i++){
            if(itemList.get(i).getName().equals("TinhTest2")){
                index = 1 ;
            }
        }
        if (index == 0)
            fail("postNewItem doesn't upload item to Server");

    }

    // Test method getItemsByCat
    @Test
    public void getItemsByCatTest() throws Exception {

        NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient( new DefaultNetworkProvider());
        List<Item> itemList = networkGotchaClient.getItemsByCat(Item.Category.SPORT) ;

        int index = 0 ;
        for (int i = 0; i < itemList.size(); i++){
            if(itemList.get(i).getName().equals("TinhTest")){
                index = 1 ;
            }
        }
        if (index == 0)
            fail("postNewItem doesn't upload item to Server");
    }

    // Test for Http connection
    @Test
    public void HttpConnectionTest() throws IOException {

        NetworkGotchaClient networkGotchaClient=new NetworkGotchaClient(new DefaultNetworkProvider());
        String urlString = networkGotchaClient.mServerUrl;
        URL object = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        int HttpResult = con.getResponseCode();
        assertEquals(HttpResult, HttpURLConnection.HTTP_OK);
    }

    // Test HTTPConnection after post request
    @Test
    public void HttpConnectionAfterPush() throws IOException {

        NetworkGotchaClient networkGotchaClient=new NetworkGotchaClient(new DefaultNetworkProvider());
        String urlString = networkGotchaClient.mServerUrl;
        URL object = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) object.openConnection();
        int HttpResult = conn.getResponseCode();
        assertEquals(HttpResult, HttpURLConnection.HTTP_OK);
    }

    @Override
    public Class<? extends Throwable> expected() {
        return null;
    }

    @Override
    public long timeout() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
