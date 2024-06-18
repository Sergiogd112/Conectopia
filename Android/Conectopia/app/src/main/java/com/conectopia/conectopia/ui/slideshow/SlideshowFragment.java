package com.conectopia.conectopia.ui.slideshow;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.data.model.LoggedInUser;
import com.conectopia.conectopia.databinding.FragmentSlideshowBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel slideshowViewModel;
    private MessageAdapter adapter;
    private List<JSONObject> messages;
    private String chatId;
    private String chatName;
    private EditText messageInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.chatId = getArguments().getString("chatId");
        this.chatName = getArguments().getString("chatName");

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((TextView) root.findViewById(R.id.chatNameTextView)).setText(chatName);
        messages = new ArrayList<>();
        ListView listView = root.findViewById(R.id.listView);

        adapter = new MessageAdapter(getContext(), messages);
        listView.setAdapter(adapter);

        messageInput = root.findViewById(R.id.messageInput);

        Button sendButton = root.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                if (message.length() > 0) {
                    connectMessagePOST(root, message);
                } else {
                    Log.i("serverTest", "Message is empty");
                }
            }
        });


        connectMessageGET(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void connectMessageGET(View root) {
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = "";
            Handler handler = new Handler();

            @Override
            public void run() {
                try {
                    String query = String.format("http://10.0.2.2:9000/api/chat?idChat=%s", chatId);
                    URL url = new URL(query);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    stream = connection.getInputStream();
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();
                    connection.disconnect();
                    Log.i("serverTest", "Result: " + result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resultObj = new JSONObject(result);
                                JSONArray jsonArray = new JSONArray(resultObj.getString("messages"));
                                messages.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    messages.add(jsonObject);
                                }
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void connectMessagePOST(View root, String message) {
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {
                try {
                    String query = String.format("http://10.0.2.2:9000/api/message");
                    Log.i("serverTest", "Query: " + query);
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
                    String urlParameters = "idChat=" + URLEncoder.encode(chatId, "UTF-8") +
                            "&content=" + URLEncoder.encode(message, "UTF-8");


                    // write the form parameters to the connection
                    conn.getOutputStream().write(urlParameters.getBytes("UTF-8"));
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
                    Log.i("serverTest", "Result: " + result);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resultObj = new JSONObject(result);
                                JSONArray jsonArray = new JSONArray(resultObj.getString("messages"));
                                messages.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    messages.add(jsonObject);
                                }
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}