package com.conectopia.conectopia.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    int count = 0;

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ListView listView;

    private ServerAdapter adapter;

    private List<JSONObject> servers;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicializa el ViewModel
        this.homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Infla el layout usando el binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create a list to store the servers
        servers = new ArrayList<>();
        ListView listView = root.findViewById(R.id.listView);

        // Create an adapter and set it to the ListView
        adapter = new ServerAdapter(getContext(), servers);
        listView.setAdapter(adapter);

        // Configura el TextView para observar cambios en el ViewModel

        connectServerGET(root);

        // Configura el botón para hacer la solicitud GET
        //root.findViewById(R.id.button).setOnClickListener(v -> connectServerGET(v));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



   /* public void plusOne () {
        View root = binding.getRoot();

        this.count++;
        // write the count to the ui MytextView
        this.homeViewModel.mText.setValue(String.valueOf(this.count));


    }*/

    public void connectServerGET(View view) {

        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

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
                    Log.i("serverTest", "Result: " + result);
                    JSONArray jsonArray = new JSONArray(result);

                    // get the list view from the root
                    handler.post(new Runnable() {
                        public void run() {
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
                                    servers.add(jsonObject);

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