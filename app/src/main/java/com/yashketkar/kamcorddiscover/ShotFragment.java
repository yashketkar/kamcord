package com.yashketkar.kamcorddiscover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ShotFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
//    private JSONObject jsonResult;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Shot> shots;
    private MyShotRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShotFragment() {
    }

    @SuppressWarnings("unused")
    public static ShotFragment newInstance(int columnCount) {
        ShotFragment fragment = new ShotFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot_list, container, false);

        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
            LinearLayout linearLayout = (LinearLayout)view;
            recyclerView =  (RecyclerView) linearLayout.findViewById(R.id.recycler_view); //(RecyclerView) view;
            progressBar = (ProgressBar) linearLayout.findViewById(R.id.progress_bar);
            final int mColumnCount = getResources().getInteger(R.integer.shot_columns);
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

            // Recycler View Scrolling Performance Fix
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            shots = new ArrayList<Shot>();
            mAdapter = new MyShotRecyclerViewAdapter(shots, mListener);
            recyclerView.setAdapter(mAdapter);
            new RestTask().execute("https://api.kamcord.com/v1/feed/ZmVlZElkPWZlZWRfZmVhdHVyZWRfc2hvdCZ1c2VySWQmdG9waWNJZCZzdHJlYW1TZXNzaW9uSWQmbGFuZ3VhZ2VDb2Rl?count=20&page=00.FEATURED_SHOTS.subfeed_featured_shots.00.00");
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Shot item);
    }


    private class RestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            try {
                return downloadContent(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task

            try {
                JSONObject jsonResult = new JSONObject(result);
                JSONArray cards = jsonResult.getJSONArray("cards");
                for (int i = 0; i < cards.length(); i++) {

                    JSONObject card = (JSONObject) cards.get(i);
                    JSONObject shotCardData = card.getJSONObject("shotCardData");

                    String id = shotCardData.getString("id");

                    JSONObject shotThumbnail = shotCardData.getJSONObject("shotThumbnail");
                    String thumburl = shotThumbnail.getString("small");
                    thumburl = thumburl.replaceAll("MOvyVys8kAi",shotCardData.getString("id"));

                    JSONObject play = shotCardData.getJSONObject("play");
                    String playurl = play.getString("mp4");

                    boolean isVideo = true;
                    if(playurl.equals("https://media.kamcord.com/content/MOvyVys8kAi/MOvyVys8kAi.mp4")){
                        isVideo = false;
                        playurl = shotThumbnail.getString("large");
                        playurl = playurl.replaceAll("MOvyVys8kAi",shotCardData.getString("id"));
                    }

                    String viewCount = shotCardData.getString("viewCount");
                    String heartCount = shotCardData.getString("heartCount");
                    String username = shotCardData.getString("username");

                    Shot s = new Shot(id, playurl, viewCount, heartCount, username, thumburl, isVideo);

                    shots.add(s);
                    Log.d(TAG, "JSON SUCCESS " + shotCardData.get("id"));
                }
                mAdapter.notifyDataSetChanged();
            }
            catch (JSONException je){
                Log.d(TAG, "JSON EXCEPTION " + je);
            }
//            catch(MalformedURLException me){
//                Log.d(TAG, "Malformed URL EXCEPTION " + me);
//            }
//            catch(IOException ioe){
//                Log.d(TAG, "IO EXCEPTION " + ioe);
//            }
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("accept-language", "en-US");
            conn.setRequestProperty("device-token", "abc123");
            conn.setRequestProperty("client-name", "android");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            // Convert the InputStream into a string
            return result.toString();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


}