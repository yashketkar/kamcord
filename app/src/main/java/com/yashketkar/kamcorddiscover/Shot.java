package com.yashketkar.kamcorddiscover;

import android.graphics.Bitmap;

/**
 * Created by yashketkar on 4/9/17.
 * This is the model class for a Screenshot.
 */

public class Shot {

    public final String id;
    public final String playurl;
    public final String viewCount;
    public final String heartCount;
    public final String username;
    public final String thumburl;
    public Bitmap thumb;
    public boolean isVideo;

    public Shot(String id, String playurl, String viewCount, String heartCount, String username, String thumburl, boolean isVideo) {
        this.id = id;
        this.playurl = playurl;
        this.viewCount = viewCount;
        this.heartCount = heartCount;
        this.username = username;
        this.thumburl = thumburl;
        this.isVideo = isVideo;
    }

    @Override
    public String toString() {
        return id;
    }

}