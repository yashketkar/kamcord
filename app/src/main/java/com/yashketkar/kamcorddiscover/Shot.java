package com.yashketkar.kamcorddiscover;

import android.graphics.Bitmap;

/**
 * Created by yashketkar on 4/6/17.
 */

public class Shot {

    public final String id;
    public final Bitmap content;
    public final String details;

    public Shot(String id, Bitmap content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return id;
    }

}
