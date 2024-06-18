package com.conectopia.conectopia.ui.createChat;

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
import com.conectopia.conectopia.databinding.FragmentCreateChatBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateChatFragment extends Fragment {


    private FragmentCreateChatBinding binding;
    private EditText chatNameInput;
    private EditText chatDescriptionInput;

    private String serverId;

    public CreateChatFragment() {
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
    public static CreateChatFragment newInstance(String param1, String param2) {
        CreateChatFragment fragment = new CreateChatFragment();
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
        this.serverId = getArguments().getString("serverId");

        // on click listener for the button createServerButton
        // this will call the connectCreateServer method
        // to create a new server
        View view = inflater.inflate(R.layout.fragment_create_chat, container, false);

        binding = FragmentCreateChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        chatNameInput = binding.chatNameInput;
        chatDescriptionInput = binding.chatDescriptionInput;

        root.findViewById(R.id.createChatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectCreateServer(v);
            }
        });


        // Inflate the layout for this fragment
        return root;
    }


    public void connectCreateServer(View view) {

        String chatName = chatNameInput.getText().toString();
        String chatDescription = chatDescriptionInput.getText().toString();
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/chat");
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
                    String urlParameters = "idServer=" + URLEncoder.encode(serverId, "UTF-8") +
                            "&chatName=" + URLEncoder.encode(chatName, "UTF-8") +
                            "&chatDescription=" + URLEncoder.encode(chatDescription, "UTF-8");

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
                                    bundle.putString("serverId", serverId);
                                    bundle.putString("chatId", jsonObject.getString("id"));
                                    bundle.putString("chatName", chatName);
                                    bundle.putString("chatDescription", chatDescription);
                                    // Use the Navigation component to navigate to the gallery view
                                    Navigation.findNavController(view).navigate(R.id.nav_slideshow, bundle);
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