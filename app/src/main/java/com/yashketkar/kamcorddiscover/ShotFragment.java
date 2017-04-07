package com.yashketkar.kamcorddiscover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yashketkar.kamcorddiscover.dummy.DummyContent;
import com.yashketkar.kamcorddiscover.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ShotFragment extends Fragment {

    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
//    private int mColumnCount = 3;
    private OnListFragmentInteractionListener mListener;
    private JSONObject jsonResult;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShotFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ShotFragment newInstance(int columnCount) {
        ShotFragment fragment = new ShotFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot_list, container, false);

        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
//            recyclerView = (RecyclerView) view;
            LinearLayout linearLayout = (LinearLayout)view;
            recyclerView =  (RecyclerView) linearLayout.findViewById(R.id.recycler_view); //(RecyclerView) view;
            progressBar = (ProgressBar) linearLayout.findViewById(R.id.progress_bar);
            final int mColumnCount = getResources().getInteger(R.integer.shot_columns);
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            new RestTask().execute("https://api.kamcord.com/v1/feed/ZmVlZElkPWZlZWRfZmVhdHVyZWRfc2hvdCZ1c2VySWQmdG9waWNJZCZzdHJlYW1TZXNzaW9uSWQmbGFuZ3VhZ2VDb2Rl?count=20&page=00.FEATURED_SHOTS.subfeed_featured_shots.00.00");
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
            //recyclerView.setAdapter(new MyShotRecyclerViewAdapter(DummyContent.ITEMS, mListener));
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Shot item);
    }


    private class RestTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // start loading animation maybe?
            //adapter.clear(); // clear "old" entries (optional)
        }

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
//            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            new ImagesTask().execute(result);
//            List<Shot> ids = new ArrayList<Shot>();
//            try {
//                jsonResult = new JSONObject(result);
//                JSONArray cards = jsonResult.getJSONArray("cards");
//                for(int i=0;i<cards.length();i++) {
//                    JSONObject card = (JSONObject) cards.get(i);
//                    JSONObject shotCardData = (JSONObject) card.getJSONObject("shotCardData");
//                    URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
//                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    ids.add(new Shot((String)shotCardData.get("id"),bmp,""));
//                    Log.d(TAG, "JSON SUCCESS " + shotCardData.get("id"));
//                }
//                recyclerView.setAdapter(new MyShotRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//                mRecyclerAdapter.setData(mData); // need to implement setter method for data in adapter
//                mRecyclerAdapter.notifyDataSetChanged();
//            }
//            catch (JSONException je){
//                Log.d(TAG, "JSON EXCEPTION " + je);
//            }
//            catch(MalformedURLException me){
//                Log.d(TAG, "Malformed URL EXCEPTION " + me);
//            }
//            catch(IOException ioe){
//                Log.d(TAG, "IO EXCEPTION " + ioe);
//            }
        }

    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

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
            String message = conn.getResponseMessage();

            is = conn.getInputStream();
            length = conn.getHeaderFieldInt("content-Length",100);

            //InputStream in = address.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
//            System.out.println(result.toString());

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is, length);
            return result.toString();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }


    private class ImagesTask extends AsyncTask<String, Void, List<Shot>> {

        @Override
        protected void onPreExecute() {
            // start loading animation maybe?
            //adapter.clear(); // clear "old" entries (optional)
        }

        @Override
        protected List<Shot> doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            List<Shot> ids = new ArrayList<Shot>();
            try {
                jsonResult = new JSONObject(params[0]);
                JSONArray cards = jsonResult.getJSONArray("cards");
                for (int i = 0; i < cards.length(); i++) {
                    JSONObject card = (JSONObject) cards.get(i);
                    JSONObject shotCardData = (JSONObject) card.getJSONObject("shotCardData");
                    JSONObject shotThumbnail = (JSONObject) shotCardData.getJSONObject("shotThumbnail");
                    URL url = new URL(shotThumbnail.getString("small"));
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    ids.add(new Shot((String) shotCardData.get("id"), bmp, ""));
                    Log.d(TAG, "JSON SUCCESS " + shotCardData.get("id"));
                }
            }
            catch (JSONException je){
                Log.d(TAG, "JSON EXCEPTION " + je);
            }
            catch(MalformedURLException me){
                Log.d(TAG, "Malformed URL EXCEPTION " + me);
            }
            catch(IOException ioe){
                Log.d(TAG, "IO EXCEPTION " + ioe);
            }
            return ids;
        }

        @Override
        protected void onPostExecute(List<Shot> result) {
            //Here you are done with the task
//            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            recyclerView.setAdapter(new MyShotRecyclerViewAdapter(result, mListener));
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

    }

}
