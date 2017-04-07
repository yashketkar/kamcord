package com.yashketkar.kamcorddiscover;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        // Get the Intent that started this activity and extract the string
        VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
        ImageView mImageView = (ImageView) findViewById(R.id.shot_image);

        Intent intent = getIntent();
        boolean isVideo = intent.getBooleanExtra(MainActivity.EXTRA_ISVIDEO, false);

        if(isVideo) {
            String message = intent.getStringExtra(MainActivity.EXTRA_URL);
            Uri uri = Uri.parse(message); //Declare your url here.
//        mVideoView.setMediaController(new MediaController(this));
            mVideoView.setMediaController(null);
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
        }
        else{
            mVideoView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
//        hideSystemUI();
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the thumb to appear under the system bars so that the thumb
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (Integer.valueOf(Build.VERSION.SDK_INT)>=16 && hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
//    }

}

