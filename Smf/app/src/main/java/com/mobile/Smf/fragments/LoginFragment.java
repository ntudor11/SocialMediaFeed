package com.mobile.Smf.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.Smf.R;
import com.mobile.Smf.activities.FeedActivity;
import com.mobile.Smf.activities.SignupActivity;
import com.mobile.Smf.database.DataInterface;

public class LoginFragment extends Fragment {

    private TextView usernameTextView;
    private TextView passwordTextView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

    private DataInterface dataInterface;

    @Override
    public void onCreate(Bundle savedInstanceState){ super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View loginView = inflater.inflate(R.layout.fragment_login, container,false);

        dataInterface = new DataInterface(getContext());

        usernameTextView = (TextView) loginView.findViewById(R.id.login_textview_username);
        usernameTextView.setText(R.string.login_username);

        passwordTextView = (TextView) loginView.findViewById(R.id.login_textview_password);
        passwordTextView.setText(R.string.login_password);

        usernameEditText = (EditText) loginView.findViewById(R.id.login_textedit_username);
        passwordEditText = (EditText) loginView.findViewById(R.id.login_textedit_password);

        loginButton = (Button) loginView.findViewById(R.id.login_button_login);
        loginButton.setText(R.string.login_button_text);
        signupButton = (Button) loginView.findViewById(R.id.login_button_signup);
        signupButton.setText(R.string.login_signup_button_text);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            String userName = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (dataInterface.checkIfValidLogin(userName,password)){
                Intent intent = new Intent(getContext(), FeedActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(),"Invalid Login", Toast.LENGTH_SHORT).show();
            }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        return loginView;
    }




}
