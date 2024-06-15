package com.conectopia.conectopia.ui.home;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.conectopia.conectopia.R;

import org.json.JSONObject;

import java.util.List;

public class ServerAdapter extends ArrayAdapter<JSONObject> {

    public ServerAdapter(@NonNull Context context, @NonNull List<JSONObject> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.server_item, parent, false);
        }

        JSONObject server = getItem(position);

        TextView serverName = convertView.findViewById(R.id.serverName);
        TextView serverDescription = convertView.findViewById(R.id.serverDescription);

        try {
            serverName.setText(server.getString("name"));
            serverDescription.setText(server.getString("description"));
            final String serverId = server.getString("id");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a bundle to hold the server id
                    Bundle bundle = new Bundle();
                    bundle.putString("serverId", serverId);

                    // Use the Navigation component to navigate to the gallery view
                    Navigation.findNavController(v).navigate(R.id.nav_gallery, bundle);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}