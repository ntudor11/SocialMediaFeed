package com.mobile.Smf.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.Smf.R;
import com.mobile.Smf.activities.FeedActivity;
import com.mobile.Smf.database.DataInterface;
import com.mobile.Smf.model.User;
import com.mobile.Smf.model.Feed; // todo remove

import java.util.List;


public class MakePicturePostFragment extends Fragment {

    private TextView textViewHeader;
    private ImageView imageViewPicture;
    private Button buttonUploadNewPost;
    private Button buttonTakePicture;

    private DataInterface dataInterface;
    private User user;

    private Bitmap imageToUploadAsBitmap;

    // purely for dev needs, todo remove
    private Feed feed;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View makePicturePostView = inflater.inflate(R.layout.fragment_makepicturepost, container, false);

        dataInterface = new DataInterface(getContext());
        user = dataInterface.getLoggedInUser();

        textViewHeader = (TextView) makePicturePostView.findViewById(R.id.makepicturepost_textview_header);
        imageViewPicture = (ImageView) makePicturePostView.findViewById(R.id.makepicturepost_imageview_picure);
        buttonUploadNewPost = (Button) makePicturePostView.findViewById(R.id.makepicturepost_button_uploadnewpostbutton);
        buttonTakePicture = (Button) makePicturePostView.findViewById(R.id.makepicturepost_button_takenewpicture);

        textViewHeader.setText(R.string.makepicturepost_header);
        buttonUploadNewPost.setText(R.string.makepicturepost_uploadbutton);
        buttonTakePicture.setText(R.string.makepicturepost_takepicturebutton);

        imageViewPicture.setImageBitmap(getPreviewImageAsBitmap());

        buttonUploadNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataInterface.uploadPicturePost(user.getUserName(), imageToUploadAsBitmap)){
                    Toast.makeText(getContext(),"Successfully posted image",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), FeedActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(),"Failed to post picture.",Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfPermissionToTakePhoto()) {
                    imageToUploadAsBitmap = takePicture();
                    updatePreviewImageView(imageToUploadAsBitmap);
                }
            }
        });

        return makePicturePostView;
    }

    private Bitmap takePicture() {

        // super WIP
        /*
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.criminalintent.fileprovider", mPhotoFile);

        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : cameraActivities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        startActivityForResult(captureImage, REQUEST_PHOTO);
        */


        return null;
    }

    private boolean checkIfPermissionToTakePhoto(){
        // todo implement
        return true;
    }



    private void updatePreviewImageView(Bitmap image){
        imageViewPicture.setImageBitmap(image);
    }

    private Bitmap getPreviewImageAsBitmap(){

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        // Log.e("derp",""+width); //1080
        // Log.e("derp",""+height); //1794


        // todo proper implementation
        return feed.createImage(width,height/2, Color.BLACK);
    }


}
