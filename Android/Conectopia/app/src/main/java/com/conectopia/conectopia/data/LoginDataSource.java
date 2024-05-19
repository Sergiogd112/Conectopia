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
        InputStream stream = null;
        String str = "";
        String result = null;
        Handler handler = new Handler();

        try {
            String query = String.format("http://10.0.2.2:9000/api/servers");
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            stream = conn.getInputStream();

            BufferedReader reader = null;

            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            conn.disconnect();
            // Mostrar resultat en el quadre de text.
            // Codi incorrecte
            // EditText n = (EditText) findViewById (R.id.edit_message);
            //n.setText(result);

            //Codi correcte
            Log.i("serverTest", result);
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}