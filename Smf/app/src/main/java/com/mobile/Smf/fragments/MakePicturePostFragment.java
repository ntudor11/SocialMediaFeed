package com.mobile.Smf.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import static android.app.Activity.RESULT_OK;


public class MakePicturePostFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int CAMERA_PERMISSION_GRANTED = 1;
    private static final int SUCCESSFULLY_RETURNED_PICTURE = 1;

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

        // set a placeholder image
        imageViewPicture.setImageBitmap(getPlaceHolderImage());

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
                    startCameraIntent();
                }
            }
        });

        return makePicturePostView;
    }


    private void startCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmapTinyPreview = (Bitmap) extras.get("data");
            imageToUploadAsBitmap = imageBitmapTinyPreview;
            updatePreviewImageView(imageToUploadAsBitmap);
        }

    }

    private boolean checkIfPermissionToTakePhoto(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)) {
                Toast.makeText(getActivity(),"App needs permission to use camera to take picture.", Toast.LENGTH_LONG).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_GRANTED);
            }
            return false;
        } else {
            // Permission has already been granted
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_GRANTED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    // permission denied
                }
                return;
            }
        }
    }


    private void updatePreviewImageView(Bitmap image){
        imageViewPicture.setImageBitmap(image);
    }

    private Bitmap getPlaceHolderImage(){

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
