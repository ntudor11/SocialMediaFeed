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
import com.mobile.Smf.database.DataInterface;
import com.mobile.Smf.model.User;

public class MakeTextPostFragment extends Fragment {

    private TextView textViewHeader;
    private EditText editTextInputText;
    private Button buttonUploadNewPost;

    private DataInterface dataInterface;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View makePostView = inflater.inflate(R.layout.fragment_maketextpost,container,false);

        dataInterface = new DataInterface(getContext());
        user = dataInterface.getLoggedInUser();

        textViewHeader = makePostView.findViewById(R.id.maketextpost_textview_header);
        editTextInputText = makePostView.findViewById(R.id.maketextpost_edittext_text);
        buttonUploadNewPost = makePostView.findViewById(R.id.maketextpost_button_uploadnewpostbutton);

        textViewHeader.setText(R.string.maketextpost_header);
        buttonUploadNewPost.setText(R.string.maketextpost_uploadbutton);

        buttonUploadNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextInputText.getText().toString();

                if (text.equals("")) {
                    Toast.makeText(getContext(),"Type something to post!",Toast.LENGTH_SHORT).show();
                } else {
                    // upload the new post and respond accordingly
                    if (dataInterface.uploadTextPost(user.getUserName(), text)) {
                        // go back to feed after posting
                        Toast.makeText(getContext(), "Uploaded post!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), FeedActivity.class);
                        startActivity(intent);
                    } else {
                        // else make a toast and let user try again?
                        Toast.makeText(getContext(), "Could not upload post, try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return makePostView;
    }
}
