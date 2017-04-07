package com.yashketkar.kamcorddiscover;

import android.graphics.Bitmap;

/**
 * Created by yashketkar on 4/6/17.
 */

public class Shot {

    public final String id;
    public final Bitmap content;
    public final String playurl;

    public Shot(String id, Bitmap content, String playurl) {
        this.id = id;
        this.content = content;
        this.playurl = playurl;
    }

    @Override
    public String toString() {
        return id;
    }

}
