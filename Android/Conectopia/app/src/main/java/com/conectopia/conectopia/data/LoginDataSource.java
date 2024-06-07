package com.conectopia.conectopia.data;

import android.os.Handler;
import android.util.Log;

import com.conectopia.conectopia.data.model.LoggedInUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        Log.i("LoginDataSource", "login: " + username + " " + password);
        InputStream stream = null;
        String str = "";
        String result = null;

        try {
            String query = String.format("http://10.0.2.2:9000/api/login?emailuname=%s&password=%s", username, password);
            URL url = new URL(query);
            Log.i("LoginDataSource", "url: " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            Log.i("LoginDataSource", "Timeout: " + conn.getReadTimeout());
            conn.setConnectTimeout(15000 /* milliseconds */);
            Log.i("LoginDataSource", "ConnectTimeout: " + conn.getConnectTimeout());
            conn.setRequestMethod("GET");
            Log.i("LoginDataSource", "Method: " + conn.getRequestMethod());
            conn.setDoInput(true);
            Log.i("LoginDataSource", "DoInput: " + conn.getDoInput());
            conn.connect();
            Log.i("LoginDataSource", "conecting to : " + url);

            stream = conn.getInputStream();


            BufferedReader reader = null;

            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            Log.i("LoginDataSource", "response: " + result);
            conn.disconnect();
            // Mostrar resultat en el quadre de text.
            // Codi incorrecte
            // EditText n = (EditText) findViewById (R.id.edit_message);
            //n.setText(result);

            // Get the PLAY token from the cookie PLAY_SESSION in the response header
            String cookie = conn.getHeaderField("Set-Cookie");
            Log.i("serverTest", cookie);
            String[] parts = cookie.split(";");
            String playToken = parts[0].substring(11);
            Log.i("serverTest", playToken);

            Log.i("serverTest", result);
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            playToken,
                            username);
            return new Result.Success<LoggedInUser>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> register(String email, String password) {
        Log.i("LoginDataSource", "register: " + email + " " + password);
        InputStream stream = null;
        String str = "";
        String result = null;

        try {
            String query = String.format("http://10.0.2.2:9000/api/register?username=%s&email=%s&password=%s", email.split("@")[0],email, password);
            URL url = new URL(query);
            Log.i("LoginDataSource", "url: " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            Log.i("LoginDataSource", "Timeout: " + conn.getReadTimeout());
            conn.setConnectTimeout(15000 /* milliseconds */);
            Log.i("LoginDataSource", "ConnectTimeout: " + conn.getConnectTimeout());
            conn.setRequestMethod("GET");
            Log.i("LoginDataSource", "Method: " + conn.getRequestMethod());
            conn.setDoInput(true);
            Log.i("LoginDataSource", "DoInput: " + conn.getDoInput());
            conn.connect();
            Log.i("LoginDataSource", "conecting to : " + url);

            stream = conn.getInputStream();


            BufferedReader reader = null;

            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            Log.i("LoginDataSource", "response: " + result);
            conn.disconnect();
            // Mostrar resultat en el quadre de text.
            // Codi incorrecte
            // EditText n = (EditText) findViewById (R.id.edit_message);
            //n.setText(result);

            // Get the PLAY token from the cookie PLAY_SESSION in the response header
            String cookie = conn.getHeaderField("Set-Cookie");
            Log.i("serverTest", cookie);
            String[] parts = cookie.split(";");
            String playToken = parts[0].substring(11);
            Log.i("serverTest", playToken);

            Log.i("serverTest", result);
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            playToken,
                            email);
            return new Result.Success<LoggedInUser>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}