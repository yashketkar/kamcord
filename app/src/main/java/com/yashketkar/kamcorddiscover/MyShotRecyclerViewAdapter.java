package com.yashketkar.kamcorddiscover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yashketkar.kamcorddiscover.ShotFragment.OnListFragmentInteractionListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Shot} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyShotRecyclerViewAdapter extends RecyclerView.Adapter<MyShotRecyclerViewAdapter.ViewHolder> {

    private final List<Shot> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyShotRecyclerViewAdapter(List<Shot> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        if(mValues.get(position).isVideo) {
            if (mValues.get(position).thumb != null) {
                holder.mThumbView.setImageBitmap(mValues.get(position).thumb);
            } else {
                new ImagesTask(holder.mThumbView).execute(mValues.get(position));
            }
//        }
//        else{
//
//        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mThumbView;
        public Shot mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbView = (ImageView) view.findViewById(R.id.thumb_image);
        }

        @Override
        public String toString() {
            return (super.toString() + " '" /*+mIdView.getText()*/ + "'");
        }

    }

    private class ImagesTask extends AsyncTask<Shot, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        private ImagesTask(ImageView imv) {
            imageViewReference = new WeakReference<ImageView>(imv);
        }

        @Override
        protected Bitmap doInBackground(Shot... params) {
            //do your request in here so that you don't interrupt the UI thread
            Bitmap bmp = null;
            try {
                URL url = new URL(params[0].thumburl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            catch(MalformedURLException me){
                Log.d(TAG, "Malformed URL EXCEPTION " + me);
            }
            catch(IOException ioe){
                Log.d(TAG, "IO EXCEPTION " + ioe);
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
//                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_launcher);
//                        imageView.setImageDrawable(placeholder);
                    }
                }
            }


        }
    }
}