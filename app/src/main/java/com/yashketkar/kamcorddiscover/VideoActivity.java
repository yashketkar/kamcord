package com.yashketkar.kamcorddiscover;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by yashketkar on 4/9/17.
 * This is the Video Activity used to play videos or display images.
 */

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        // Get the Intent that started this activity and extract the string
        final VideoView mVideoView = (VideoView) findViewById(R.id.video_view);
        final ImageView mImageView = (ImageView) findViewById(R.id.shot_image);
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.video_progress_bar);
        TextView mTextView = (TextView) findViewById(R.id.video_meta_text);

        Intent intent = getIntent();
        boolean isVideo = intent.getBooleanExtra(MainActivity.EXTRA_ISVIDEO, false);
        String playurl = intent.getStringExtra(MainActivity.EXTRA_URL);
        String heartsCount = intent.getStringExtra(MainActivity.EXTRA_HC);
        String viewsCount = intent.getStringExtra(MainActivity.EXTRA_VC);
        String metaText = MyShotRecyclerViewAdapter.getEmojiByUnicode(MyShotRecyclerViewAdapter.heartEmoji) + "\n" + heartsCount + "\n" + MyShotRecyclerViewAdapter.getEmojiByUnicode(MyShotRecyclerViewAdapter.eyesEmoji) + "\n" + viewsCount;

        mTextView.setText(metaText);

        if (isVideo) {
            Uri uri = Uri.parse(playurl);
            mVideoView.setMediaController(null);
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mProgressBar.setVisibility(View.GONE);
                    mVideoView.requestFocus();
                    mVideoView.start();
                }
            });
        } else {
            new ImagesTask(mImageView).execute(playurl);
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
        hideSystemUI();
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the thumb to appear under the system bars so that the thumb
        // doesn't resize when the system bars hide and show.
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            );
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }
}