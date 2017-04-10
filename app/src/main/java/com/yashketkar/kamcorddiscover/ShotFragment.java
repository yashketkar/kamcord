package com.yashketkar.kamcorddiscover;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class ShotFragment extends Fragment {

    public static String TAG = "com.yashketkar";
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textView;
    private List<Shot> shots;
    private MyShotRecyclerViewAdapter mAdapter;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

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
            final Context context = view.getContext();
            LinearLayout linearLayout = (LinearLayout) view;
            recyclerView = (RecyclerView) linearLayout.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) linearLayout.findViewById(R.id.progress_bar);
            textView = (TextView) linearLayout.findViewById(R.id.empty_view);
            final int mColumnCount = getResources().getInteger(R.integer.shot_columns);
            GridLayoutManager gm = new GridLayoutManager(context, mColumnCount);
            recyclerView.setLayoutManager(gm);

            // Recycler View Scrolling Performance Fix
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            shots = new ArrayList<>();
            mAdapter = new MyShotRecyclerViewAdapter(shots, mListener);
            recyclerView.setAdapter(mAdapter);

            scrollListener = new EndlessRecyclerViewScrollListener(gm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    loadNextDataFromApi(totalItemsCount);
                }
            };
            // Adds the scroll listener to RecyclerView
            recyclerView.addOnScrollListener(scrollListener);

            new RestTask().execute("https://api.kamcord.com/v1/feed/ZmVlZElkPWZlZWRfZmVhdHVyZWRfc2hvdCZ1c2VySWQmdG9waWNJZCZzdHJlYW1TZXNzaW9uSWQmbGFuZ3VhZ2VDb2Rl?count=20&page=00.FEATURED_SHOTS.subfeed_featured_shots.00.00");
        }
        return view;
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        new RestTask().execute("https://api.kamcord.com/v1/feed/ZmVlZElkPWZlZWRfZmVhdHVyZWRfc2hvdCZ1c2VySWQmdG9waWNJZCZzdHJlYW1TZXNzaW9uSWQmbGFuZ3VhZ2VDb2Rl?count=20&page=" + offset + ".FEATURED_SHOTS.subfeed_featured_shots." + offset + "." + offset);
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
            while ((line = reader.readLine()) != null) {
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
                return "fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
            if (result.equals("fail")) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray cards = jsonResult.getJSONArray("cards");
                    for (int i = 0; i < cards.length(); i++) {

                        JSONObject card = (JSONObject) cards.get(i);
                        JSONObject shotCardData = card.getJSONObject("shotCardData");

                        String id = shotCardData.getString("id");

                        JSONObject shotThumbnail = shotCardData.getJSONObject("shotThumbnail");
                        String thumburl = shotThumbnail.getString("small");
                        thumburl = thumburl.replaceAll("MOvyVys8kAi", shotCardData.getString("id"));

                        JSONObject play = shotCardData.getJSONObject("play");
                        String playurl = play.getString("mp4");

                        boolean isVideo = true;
                        if (playurl.equals("https://media.kamcord.com/content/MOvyVys8kAi/MOvyVys8kAi.mp4")) {
                            isVideo = false;
                            playurl = shotThumbnail.getString("large");
                            playurl = playurl.replaceAll("MOvyVys8kAi", shotCardData.getString("id"));
                        }
                        String viewCount = shotCardData.getString("viewCount");
                        String heartCount = shotCardData.getString("heartCount");
                        String username = shotCardData.getString("username");

                        Shot s = new Shot(id, playurl, viewCount, heartCount, username, thumburl, isVideo);

                        shots.add(s);
                        mAdapter.notifyItemInserted(shots.size() - 1);
                        Log.d(TAG, "JSON SUCCESS " + shotCardData.get("id"));
                    }
                } catch (JSONException je) {
                    Log.d(TAG, "JSON EXCEPTION " + je);
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}