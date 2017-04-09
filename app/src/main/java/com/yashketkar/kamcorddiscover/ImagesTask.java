package com.yashketkar.kamcorddiscover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yashketkar on 4/9/17.
 * This is used for loading of thumbnails and other images asynchronously.
 */

public class ImagesTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private Shot s;

    public ImagesTask(ImageView imv) {
        imageViewReference = new WeakReference<>(imv);
    }

    public ImagesTask(ImageView imv, Shot s) {
        imageViewReference = new WeakReference<>(imv);
        this.s = s;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        //do your request in here so that you don't interrupt the UI thread
        Bitmap bmp = null;
        try {
            URL url = new URL(params[0]);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException me) {
            Log.d(ShotFragment.TAG, "Malformed URL EXCEPTION " + me);
        } catch (IOException ioe) {
            Log.d(ShotFragment.TAG, "IO EXCEPTION " + ioe);
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(s!=null){
            s.thumb = bitmap;
        }
        ImageView imageView = imageViewReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}