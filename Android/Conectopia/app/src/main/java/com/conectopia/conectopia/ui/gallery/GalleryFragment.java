package com.conectopia.conectopia.ui.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private ChatAdapter adapter;

    private List<JSONObject> chats;
    private String serverId;
    private String serverName;
    private String serverDescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.serverId = getArguments().getString("serverId");
        this.serverName = getArguments().getString("serverName");

        this.serverDescription = getArguments().getString("serverDescription");
        // Inicializa el ViewModel
        this.galleryViewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

        // Infla el layout usando el binding
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((TextView) root.findViewById(R.id.serverNameTextView)).setText(serverName);
        ((TextView) root.findViewById(R.id.descriptionTextView)).setText(serverDescription);        // Create a list to store the servers
        chats = new ArrayList<>();
        ListView listView = root.findViewById(R.id.listView);

        // Create an adapter and set it to the ListView
        adapter = new ChatAdapter(getContext(), chats);
        listView.setAdapter(adapter);

        Button createChatButton = root.findViewById(R.id.createChatButton);
        createChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("serverId", serverId);
                Navigation.findNavController(v).navigate(R.id.createChatFragment, bundle);
            }
        });
        // Configura el TextView para observar cambios en el ViewModel


        connectChatGET(root);

        // Configura el botón para hacer la solicitud GET
        //root.findViewById(R.id.button).setOnClickListener(v -> connectServerGET(v));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void connectChatGET(View view) {

        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/server?idServer=%s", serverId);
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
                    Log.i("serverTest", "Result: " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("chats"));

                    // get the list view from the root
                    handler.post(new Runnable() {
                        public void run() {
                            chats.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String description = jsonObject.getString("description");
                                    Log.i("serverTest", "Server Name: " + name);
                                    Log.i("serverTest", "Description: " + description);
                                    // create a new card-like element in the list view
                                    // the name of the server is the title
                                    // the description of the server is the paragraph
                                    chats.add(jsonObject);

                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                }

                            }
                            adapter.notifyDataSetChanged();
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

