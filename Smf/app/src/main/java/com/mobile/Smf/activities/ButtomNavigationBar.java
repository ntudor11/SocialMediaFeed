package com.mobile.Smf.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.BottomNavigationView;
import android.widget.ImageView;


import com.mobile.Smf.R;
import com.mobile.Smf.database.DataInterface;

public class ButtomNavigationBar extends Fragment {

    //DataInterface dataInterface;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    Intent feedIntent = new Intent(getContext(), FeedActivity.class);
                    startActivity(feedIntent);
                case R.id.navigation_makepost:
                    makePostDialog(getContext(), navigation);
                //case R.id.navigation_account:
                    //Intent accountIntent = new Intent(getContext(), Account.class);
                    //startActivity(accountIntent);
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dataInterface = new DataInterface(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View navigationView = inflater.inflate(R.layout.fragment_navigationbar, container, false);

        navigation = (BottomNavigationView) navigationView.findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return navigationView;
    }


    public void makePostDialog(Context context, BottomNavigationView view) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.makepost_dialog);

        ImageView text = dialog.findViewById(R.id.textpost);
        ImageView photo = dialog.findViewById(R.id.photopost);

        Drawable textIcon = text.getDrawable();
        textIcon.setBounds(0, 0, 50, 50);
        Drawable photoIcon = photo.getDrawable();
        photoIcon.setBounds(0, 0, 50, 50);

        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent textIntent = new Intent(getContext(), MakeTextPostActivity.class);
                startActivity(textIntent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(getContext(), MakePicturePostActivity.class);
                startActivity(photoIntent);
            }
        });

        view.setOnClickListener(new BottomNavigationView.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                view.setOnClickListener(null);
            }
        });


    }

}
