package com.conectopia.conectopia.ui.profile;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.conectopia.conectopia.R;
import com.conectopia.conectopia.data.model.LoggedInUser;
import com.conectopia.conectopia.databinding.FragmentProfileBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView userNameView;
    private TextView emailView;
    private FragmentProfileBinding binding;
    private EditText userNameInput;
    private EditText confirmUserNameInput;
    private EditText emailInput;
    private EditText confirmEmailInput;
    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private EditText confirmNewPasswordInput;

    private String serverId;

    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userNameInput = binding.newUsernameInput;
        confirmUserNameInput = binding.confirmNewUsernameInput;
        emailInput = binding.newEmailInput;
        confirmEmailInput = binding.confirmNewEmailInput;
        oldPasswordInput = binding.oldPasswordInput;
        newPasswordInput = binding.newPasswordInput;
        confirmNewPasswordInput = binding.confirmNewPasswordInput;

        userNameView = binding.profileUsernameTextView;
        emailView = binding.profileEmailTextView;


        root.findViewById(R.id.changeUsernameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserName(v);
            }
        });
        root.findViewById(R.id.changeEmailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail(v);
            }
        });
        root.findViewById(R.id.changePasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(v);
            }
        });

        connectProfileGET(root);

        // Inflate the layout for this fragment
        return root;
    }

    public void changeUserName(View view) {
        String newUsername = userNameInput.getText().toString();
        String confirmNewUsername = confirmUserNameInput.getText().toString();
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/user");
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
                    String urlParameters = "newUsername=" + URLEncoder.encode(newUsername, "UTF-8") +
                            "&confirmNewUsername=" + URLEncoder.encode(confirmNewUsername, "UTF-8");

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
                    Log.i("serverTest", "Result: " + result);
                    JSONObject jsonObject = new JSONObject(result);

                    handler.post(new Runnable() {
                        public void run() {
                            if (jsonObject.has("error")) {
                                try {
                                    Log.e("serverTest", "Error: " + jsonObject.getString("error"));
                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                connectProfileGET(view);
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

    public void changeEmail(View view) {
        String newEmail = emailInput.getText().toString();
        String confirmNewEmail = confirmEmailInput.getText().toString();
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/user");
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
                    String urlParameters = "newEmail=" + URLEncoder.encode(newEmail, "UTF-8") +
                            "&confirmNewEmail=" + URLEncoder.encode(confirmNewEmail, "UTF-8");

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
                    Log.i("serverTest", "Result: " + result);
                    JSONObject jsonObject = new JSONObject(result);

                    handler.post(new Runnable() {
                        public void run() {
                            if (jsonObject.has("error")) {
                                try {
                                    Log.e("serverTest", "Error: " + jsonObject.getString("error"));
                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                connectProfileGET(view);
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

    public void changePassword(View view) {
        String oldPassword = oldPasswordInput.getText().toString();
        String newPassword = newPasswordInput.getText().toString();
        String confirmNewPassword = confirmNewPasswordInput.getText().toString();
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/user");
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
                    String urlParameters = "oldPassword=" + URLEncoder.encode(oldPassword, "UTF-8") +
                            "&newPassword=" + URLEncoder.encode(newPassword, "UTF-8") +
                            "&confirmNewPassword=" + URLEncoder.encode(confirmNewPassword, "UTF-8");

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
                    JSONObject jsonObject = new JSONObject(result);

                    handler.post(new Runnable() {
                        public void run() {
                            if (jsonObject.has("error")) {
                                try {
                                    Log.e("serverTest", "Error: " + jsonObject.getString("error"));
                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                connectProfileGET(view);
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

    public void connectProfileGET(View view) {
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {
                    String query = String.format("http://10.0.2.2:9000/api/user");
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Cookie", LoggedInUser.getInstance().getPlayToken());
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
                    Log.i("serverTest", "Result: " + result);
                    JSONObject jsonObject = new JSONObject(result);

                    handler.post(new Runnable() {
                        public void run() {
                            if (jsonObject.has("error")) {
                                try {
                                    Log.e("serverTest", "Error: " + jsonObject.getString("error"));
                                } catch (Exception e) {
                                    Log.e("serverTest", "Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    userNameView.setText(jsonObject.getString("username"));
                                    emailView.setText(jsonObject.getString("email"));
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