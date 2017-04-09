package com.yashketkar.kamcorddiscover;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yashketkar.kamcorddiscover.ShotFragment.OnListFragmentInteractionListener;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Shot} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyShotRecyclerViewAdapter extends RecyclerView.Adapter<MyShotRecyclerViewAdapter.ViewHolder> {

    public final static int heartEmoji = 0x2764;
    public final static int eyesEmoji = 0x1F440;
    private final List<Shot> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyShotRecyclerViewAdapter(List<Shot> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
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
        holder.mTextView.setText(getEmojiByUnicode(heartEmoji) + " " + holder.mItem.heartCount + " " + getEmojiByUnicode(eyesEmoji) + " " + holder.mItem.viewCount);
        if (holder.mItem.thumb != null) {
            holder.mThumbView.setImageBitmap(holder.mItem.thumb);
        } else {
            new ImagesTask(holder.mThumbView, holder.mItem).execute(holder.mItem.thumburl);
        }
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
        public final TextView mTextView;
        public Shot mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbView = (ImageView) view.findViewById(R.id.thumb_image);
            mTextView = (TextView) view.findViewById(R.id.meta_text);
        }
    }
}