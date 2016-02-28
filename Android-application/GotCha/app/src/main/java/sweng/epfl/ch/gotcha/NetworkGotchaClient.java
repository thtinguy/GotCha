package sweng.epfl.ch.gotcha;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class NetworkGotchaClient implements GotchaClient {
    public static String mServerUrl = "https://serene-cliffs-2486.herokuapp.com/";

    private final static int HTTP_SUCCESS_START = 200;
    private final static int HTTP_SUCCESS_END = 299;

    private final static String PATH_USER_FORM = "users/";
    private final static String PATH_ITEM_FORM = "items/";

    private final static String PATH_MESSAGE_FORM = "messages/";
    private final static String PATH_LIST_ITEMS = "items/category/";


    private final static String PATH_WISHES_FORM = "wishes";
    private final static String PATH_DELETE = "delete";


    private final static String PATH_SERACH_FROM = "items/all/filter?keyword=";
    private final DefaultNetworkProvider mNetworkProvider;


    public NetworkGotchaClient(DefaultNetworkProvider networkProvider) {
        mNetworkProvider = networkProvider;
    }

    public HttpURLConnection connection(URL url, Request request) throws IOException, ClientException {

        HttpURLConnection conn = mNetworkProvider.getConnection(url);
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod(request.toString());
        conn.setDoInput(true);

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept", "text/plain");
        // Starts the query
       /* if (request == Request.GET) {
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("response", String.valueOf(response));
            if (response < HTTP_SUCCESS_START || response > HTTP_SUCCESS_END) {
                throw new ClientException();
            }
        }*/
        return conn;

    }

    public String getData(HttpURLConnection conn) throws ClientException, IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append("\n");
            }

            String result = out.toString();
            Log.d("HTTPFetchContent", "Fetched string of length "
                    + result.length());
            return result;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void postItem(String item) throws ClientException, IOException {
        String url = mServerUrl + PATH_ITEM_FORM.substring(0, PATH_ITEM_FORM.length() - 1);
        URL urlObject = new URL(url);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(item);
                wr.flush();
                StringBuilder sb = new StringBuilder();
                int HttpResult = conn.getResponseCode();
                if (HttpResult == 201) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    br.close();
                } else {
                    Log.d("Connection failed; ", String.valueOf(HttpResult));
                    throw new ClientException();
                }

            }
        }

    }

    public HttpURLConnection checkCookie(URL urlObject, Request request) throws IOException, ClientException {

        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String cookie = prefs.getString("Cookie", null);
        String cookieHeader = "";
        if (cookie != null) {
            cookieHeader = cookie.replace("; Path=/; HTTPOnly", "");

            HttpURLConnection conn = connection(urlObject, request);
            conn.setRequestProperty("Cookie", cookieHeader);
            if (request == Request.POST) {
                conn.setDoOutput(true);
            }
            return conn;
        }

        return null;
    }

    @Override
    public int postMessage(String item) throws IOException, ClientException {
        String url = mServerUrl + PATH_MESSAGE_FORM.substring(0, PATH_MESSAGE_FORM.length() - 1);
        URL urlObject = new URL(url);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        int HttpResult = -1;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(item);
                wr.flush();
                StringBuilder sb = new StringBuilder();
                HttpResult = conn.getResponseCode();
                if (HttpResult == 201) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    br.close();
                } else {
                    Log.d("", String.valueOf(HttpResult));
                }

            } else {
                Log.d("you are ", "not logged in");
            }
        }

        Log.d("", String.valueOf(HttpResult));
        return HttpResult;
    }


    @Override
    public List<Item> getItemsByCat(Item.Category category) throws Exception {

        URL url = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
        int userId = prefs.getInt("UserId", 0);
        HttpsURLConnection conn;
        if (category == Item.Category.MYITEMS) {
//                String userid = User.getUserId().toString() + "/";
            url = new URL(mServerUrl + PATH_USER_FORM + String.valueOf(userId) + "/" + PATH_ITEM_FORM.substring(0, PATH_ITEM_FORM.length() - 1));
            conn = (HttpsURLConnection) checkCookie(url, Request.GET);
        } else if (category == Item.Category.WISHLIST) {
            url = new URL(mServerUrl + PATH_USER_FORM + String.valueOf(userId) + PATH_WISHES_FORM);

            conn = (HttpsURLConnection) checkCookie(url, Request.GET);
        } else {
            url = new URL(mServerUrl + PATH_LIST_ITEMS + category);
            conn = (HttpsURLConnection) connection(url, Request.GET);

        }

        String toParse = getData(conn).toString();
        List<Item> itemList = new ArrayList();
        JSONArray jsonArray = new JSONArray(toParse);
        for (int i = 0; i < jsonArray.length(); i++) {
            itemList.add(Item.fromJSON(jsonArray.getJSONObject(i)));
        }
        return itemList;

    }

    public User getUserById(long id) throws Exception {
        try {
            URL url = new URL(mServerUrl + PATH_USER_FORM + id);
            HttpURLConnection conn = connection(url, Request.GET);
            String toParse = getData(conn).toString();
            JSONObject jsonObject = new JSONObject(toParse);
            User user = User.fromJSON(jsonObject);
            return user;
        } catch (IOException e) {
            throw new Exception();
        }
    }


    public List<Item> getSearchResult(String keyword) throws Exception {
        Log.d("gotcha client: ", mServerUrl + PATH_SERACH_FROM + keyword);

        try {
            URL url = new URL(mServerUrl + PATH_SERACH_FROM + keyword);
            HttpURLConnection conn = connection(url, Request.GET);
            String toParse = getData(conn).toString();
            List<Item> itemList = new ArrayList();
            JSONArray jsonArray = new JSONArray(toParse);
            for (int i = 0; i < jsonArray.length(); i++) {
                itemList.add(Item.fromJSON(jsonArray.getJSONObject(i)));
            }
            return itemList;
        } catch (IOException e) {
            throw new Exception();
        }
    }


    @Override
    public Item getItemById(Long id) throws Exception {

        URL url = new URL(mServerUrl + PATH_ITEM_FORM + id);
        HttpURLConnection conn = connection(url, Request.GET);
        String toParse = getData(conn).toString();
        JSONObject jsonObject = new JSONObject(toParse);
        Item item = Item.fromJSON(jsonObject);
        return item;

    }

    @Override
    public List<Message> getMessages(Long receiverId) throws Exception {

        URL urlObject = new URL(mServerUrl + PATH_USER_FORM + String.valueOf(receiverId) + "/" + "messages");
        HttpURLConnection conn = checkCookie(urlObject, Request.GET);
        if (conn != null) {
            String toParse = getData(conn).toString();
            List<Message> messageList = new ArrayList();
            JSONArray jsonArray = new JSONArray(toParse);
            for (int i = 0; i < jsonArray.length(); i++) {
                messageList.add(Message.fromJSON(jsonArray.getJSONObject(i)));
            }

            return messageList;
        }
        return null;
    }

    @Override
    public String getUserNameById(Long userId) throws Exception {
        try {
            URL url = new URL(mServerUrl + PATH_USER_FORM + String.valueOf(userId));
            HttpURLConnection conn = connection(url, Request.GET);
            String toParse = getData(conn).toString();
            JSONObject jsonUser = new JSONObject(toParse);
            User user = User.fromJSON(jsonUser);
            String userName = user.getFirstName() + " " + user.getLastName();
            return userName;
        } catch (IOException e) {
            throw new Exception();
        }
    }


    public List<Wish> getWishList() throws Exception {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
        int userId = prefs.getInt("UserId", 0);
        URL url = new URL(mServerUrl + PATH_USER_FORM + userId + "/" + PATH_WISHES_FORM);
        HttpURLConnection conn = checkCookie(url, Request.GET);
        List<Wish> wishList = new ArrayList();
        if (conn != null) {
            String toParse = getData(conn).toString();


            JSONArray jsonArray = new JSONArray(toParse);
            for (int i = 0; i < jsonArray.length(); i++) {
                wishList.add(Wish.fromJSON(jsonArray.getJSONObject(i)));
            }
        }
        return wishList;

    }

    public void postWish(String keyword) throws IOException, ClientException {
        String url = mServerUrl + PATH_WISHES_FORM;
        URL urlObject = new URL(url);
        String query = "keyword=" + keyword;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {
                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
                int HttpResult = conn.getResponseCode();
                if (HttpResult != 201) {
                    throw new ClientException();
                }

            }
        }
    }

    public void deleteItem(Integer itemId) throws IOException, ClientException {
        String url = mServerUrl + PATH_ITEM_FORM + PATH_DELETE;
        URL urlObject = new URL(url);
        String query = "itemId=" + itemId;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {
                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
                int HttpResult = conn.getResponseCode();
                if (HttpResult != 200) {
                    throw new ClientException();
                }

            }
        }
    }

    public void removeWish(Integer wishId) throws IOException, ClientException {
        String url = mServerUrl + PATH_WISHES_FORM + "/" + PATH_DELETE;
        URL urlObject = new URL(url);
        String query = "wishId=" + wishId;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {
                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
                int HttpResult = conn.getResponseCode();
                if (HttpResult != 200) {
                    throw new ClientException();
                }

            }
        }
    }

    public int deleteMessage(Message message) throws IOException, ClientException {
        String url = mServerUrl + PATH_MESSAGE_FORM + "delete";
        URL urlObject = new URL(url);
        String query = "messageId=" + message.getMessageId();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        int HttpResult = -1;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {

                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
                HttpResult = conn.getResponseCode();
                if (HttpResult != 200) {
                    throw new ClientException();
                }
            }
        }

        return HttpResult;
    }

    public void openedMessage(Long messageId) throws IOException, ClientException {
        String url = mServerUrl + PATH_MESSAGE_FORM + "open";
        URL urlObject = new URL(url);
        String query = "messageId=" + messageId;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn = checkCookie(urlObject, Request.POST);
            if (conn != null) {

                Writer writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();
                int HttpResult = conn.getResponseCode();
                Log.d("http response", String.valueOf(HttpResult));
                if (HttpResult != 200) {
                    throw new ClientException();
                }

            }
        }
    }

    /*
        Help method for testing
        sets the url to the test server
     */
    public void setmServerUrl(String url) {
        mServerUrl = url;
    }

    public enum Request {GET, POST}

}


