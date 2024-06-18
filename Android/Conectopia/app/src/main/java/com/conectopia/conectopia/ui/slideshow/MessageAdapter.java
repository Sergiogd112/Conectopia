package com.conectopia.conectopia.ui.slideshow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.conectopia.conectopia.R;

import org.json.JSONObject;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<JSONObject> {

    public MessageAdapter(@NonNull Context context, @NonNull List<JSONObject> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
        }

        JSONObject message = getItem(position);
        Log.d("MessageAdapter", message.toString());

        TextView messageAuthor = convertView.findViewById(R.id.messageAuthor);
        TextView messageContent = convertView.findViewById(R.id.messageContent);

        TextView messageDate = convertView.findViewById(R.id.dateView);
        TextView messageRole = convertView.findViewById(R.id.roleView);

        try {
            messageAuthor.setText(message.getJSONObject("user").getString("username"));
            messageContent.setText(message.getString("content"));
            messageDate.setText(message.getString("date"));
            messageRole.setText(message.getJSONObject("user").getString("role"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}