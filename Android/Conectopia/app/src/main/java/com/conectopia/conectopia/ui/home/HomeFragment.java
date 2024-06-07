package com.conectopia.conectopia.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.databinding.FragmentHomeBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {
    int count = 0;

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicializa el ViewModel
        this.homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Infla el layout usando el binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configura el TextView para observar cambios en el ViewModel



        // Configura el botón para hacer la solicitud GET
        //root.findViewById(R.id.button).setOnClickListener(v -> connectServerGET(v));

        return root;
    }


    @Override   public void onDestroyView() {
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

                    //Codi correcte
                    Log.i("serverTest", result);
                    handler.post(new Runnable() {
                        public void run() {
                            homeViewModel.mText.setValue(String.valueOf(result));
                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}