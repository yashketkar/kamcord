package com.yashketkar.kamcorddiscover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements ShotFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_URL = "com.yashketkar.kamcorddiscover.URL";
    public static final String EXTRA_ISVIDEO = "com.yashketkar.kamcorddiscover.ISVIDEO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onListFragmentInteraction(Shot item) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(EXTRA_URL, item.playurl);
        intent.putExtra(EXTRA_ISVIDEO, item.isVideo);
        startActivity(intent);
    }

}