package sweng.epfl.ch.gotcha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserProfileTequila extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_profile_tequila);
        TextView userIdView = (TextView) findViewById(R.id.userId);
        TextView sciperView = (TextView) findViewById(R.id.sciper) ;
        TextView gasparView = (TextView) findViewById(R.id.gaspar) ;
        TextView emailView = (TextView) findViewById(R.id.email) ;
        TextView firtNameView = (TextView) findViewById(R.id.firstname);
        TextView lastNameView = (TextView) findViewById(R.id.lastname) ;
        Uri data = getIntent().getData();
        String path = data.getScheme() + data.getHost() + data.getQuery();
        String code = extractCode(path);
        String serverUrl=new NetworkGotchaClient(new DefaultNetworkProvider()).mServerUrl;
        String newpath = serverUrl + "login?code=" + code;
        DownloadWebpageTask task = new DownloadWebpageTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userProfile = null;
        try {
            userProfile = task.execute(newpath).get();
            JSONObject jsonObject = new JSONObject(userProfile) ;
            int userId = jsonObject.getInt("userId");
            String sciper = jsonObject.getString("sciper") ;
            String gaspar = jsonObject.getString("gaspar");
            String email = jsonObject.getString("email");
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName") ;
            prefs.edit().putInt("UserId",userId).commit() ;
           // textView.setText(userProfile);
            userIdView.setText("UserId : " + String.valueOf(userId));
            sciperView.setText("Sciper : " + sciper);
            gasparView.setText("Gaspar : " + gaspar);
            emailView.setText("Email : " + email );
            firtNameView.setText("First name : " + firstName );
            lastNameView.setText("Last name : " + lastName  );

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Go back to main activity when press button
        Button back = (Button) findViewById(R.id.backfromuserprofile);;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(v.getContext(), MainActivity.class);
                startActivity(k);
            }
        });

    }


    public static String extractCode(String redirectUri) {
        String marker = "code=";
        return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {

                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid."+urls[0].toString();
            }
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            textView.setText(result);

        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        final String COOKIES_HEADER = "Set-Cookie";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
            MainActivity.        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    prefs.edit().putString("Cookie", cookie).commit();

                }
            }


            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


}

