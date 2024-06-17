package com.conectopia.conectopia.ui.createServer;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.data.model.LoggedInUser;
import com.conectopia.conectopia.databinding.FragmentCreateServerBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateServerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateServerFragment extends Fragment {


    private FragmentCreateServerBinding binding;
    private EditText serverNameInput;
    private EditText serverDescriptionInput;


    public CreateServerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateServerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateServerFragment newInstance(String param1, String param2) {
        CreateServerFragment fragment = new CreateServerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // on click listener for the button createServerButton
        // this will call the connectCreateServer method
        // to create a new server
        View view = inflater.inflate(R.layout.fragment_create_server, container, false);

        binding = FragmentCreateServerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        serverNameInput = binding.serverNameInput;
        serverDescriptionInput = binding.serverDescriptionInput;

        root.findViewById(R.id.createServerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectCreateServer(v);
            }
        });


        // Inflate the layout for this fragment
        return root;
    }


    public void connectCreateServer(View view) {

        String serverName = serverNameInput.getText().toString();
        String serverDescription = serverDescriptionInput.getText().toString();
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/server");
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Accept", "application/json");
                    Log.i("serverTest", "Token: " + LoggedInUser.getInstance().getPlayToken());
                    // add the cookie
                    conn.setRequestProperty("Cookie", LoggedInUser.getInstance().getPlayToken());

                    // add the form parameters
                    String urlParameters = "serverName=" + URLEncoder.encode(serverName, "UTF-8") +
                            "&serverDescription=" + URLEncoder.encode(serverDescription, "UTF-8");

                    // write the form parameters to the connection
                    conn.getOutputStream().write(urlParameters.getBytes("UTF-8"));
                    conn.connect();
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
                    Log.i("serverTest", "Result: " + result);
                    JSONObject jsonObject = new JSONObject(result);

                    // get the list view from the root
                    handler.post(new Runnable() {
                        public void run() {
                            // check if there is an error
                            if (jsonObject.has("error")) {
                                try {
                                    Log.e("serverTest", "Error: " + jsonObject.getString("error"));
                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                // navigate to gallery
                                Bundle bundle = new Bundle();
                                try {
                                    bundle.putString("serverId", jsonObject.getString("id"));
                                    bundle.putString("serverName", serverName);
                                    bundle.putString("serverDescription", serverDescription);
                                    // Use the Navigation component to navigate to the gallery view
                                    Navigation.findNavController(view).navigate(R.id.nav_gallery, bundle);
                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                    e.printStackTrace();
                                }

                            }

                        }
                    });

                } catch (Exception e) {
                    Log.e("serverTest", "Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}