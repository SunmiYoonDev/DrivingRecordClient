package com.comp306.driving_record_api_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RecordListActivity extends AppCompatActivity {

    private AppCompatActivity activity = RecordListActivity.this;
    Context context = RecordListActivity.this;
    private RecyclerView recyclerViewRecords;
    private ArrayList<Record> recordsList = new ArrayList<>();
    private RecordsRecyclerAdapter beneficiaryRecyclerAdapter;
    SearchView searchBox;
    private ArrayList<Record> filteredList;
    private boolean useRFC5179CompatibilityMode = true;
    private DrivingRecordAPIClient histClient;
    private static final String TAG = RecordListActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

        Intent intentThatStartedThisActivity = getIntent();
//        if (intentThatStartedThisActivity.hasExtra("NAME")) {
//
//            //get all needed extras intent
//
//            int id = getIntent().getExtras().getInt("ID");
//            String email = getIntent().getExtras().getString("EMAIL");
//            String address = getIntent().getExtras().getString("ADDRESS");
//            String country = getIntent().getExtras().getString("COUNTRY");
//
//
//        } else {
//
//            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
//
//        }
        histClient = DrivingRecordAPIClient.getInstance();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    //Loop and parse through the response to get our data
                    JSONArray array = response.getJSONArray("features");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject record = array.getJSONObject(i).getJSONObject("attributes");
                        Log.e(TAG,"Occurrence Date: %s %s, %s" +
                                record.getString("rid") +
                                record.getString("licenseID") +
                                record.getString("ltype"));
                        //Create BitmapDescriptor that will be the icon for each record
//                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(android.R.drawable.btn_star);
//                        //Create a marker on the map and set the title, icon and snippet
//                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(record.getDouble("Lat"), record.getDouble("Long"))));
//                        marker.setTitle(record.getString("MCI"));
//                        marker.setIcon(icon);
//                        marker.setSnippet(String.format("Occurrence Date: %s %s, %s", record.getString("occurrencemonth"), record.getString("occurrenceday"), record.getString("occurrenceyear")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to retrieve response: ", e);
                }
            }

            /**
             * Returns Object of type {@link JSONObject}, {@link JSONArray}, String, Boolean, Integer, Long,
             * Double or {@link JSONObject#NULL}, see {@link JSONTokener#nextValue()}
             *
             * @param responseBody response bytes to be assembled in String and parsed as JSON
             * @return Object parsedResponse
             * @throws JSONException exception if thrown while parsing JSON
             */
            @Override
            protected Object parseResponse(byte[] responseBody) throws JSONException {
                if (null == responseBody)
                    return null;
                Object result = null;
                //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If JSON is not valid this will return null
                String jsonString = getResponseString(responseBody, getCharset());
                if (jsonString != null) {
                    jsonString = jsonString.trim();
                    if (useRFC5179CompatibilityMode) {
                        if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
                            result = new JSONTokener(jsonString).nextValue();
                        }
                    } else {
                        // Check if the string is an JSONObject style {} or JSONArray style []
                        // If not we consider this as a string
                        if ((jsonString.startsWith("{") && jsonString.endsWith("}"))
                                || jsonString.startsWith("[") && jsonString.endsWith("]")) {
                            result = new JSONTokener(jsonString).nextValue();
                        }
                        // Check if this is a String "my String value" and remove quote
                        // Other value type (numerical, boolean) should be without quote
                        else if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                            result = jsonString.substring(1, jsonString.length() - 1);
                        }
                    }
                }
                if (result == null) {
                    result = jsonString;
                }
                parseRecords((JSONArray) result);
                return result;
            }

            /**
             * Returns when request failed
             *
             * @param statusCode    http response status line
             * @param headers       response headers if any
             * @param throwable     throwable describing the way request failed
             * @param errorResponse parsed response if any
             */
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }


        };
        //Call the CrimeHistoryClient.getData() method from the singleton with the JsonHttpResponseHandler instance as the parameter
        histClient.getData(handler);
        initObjects();
    }

    private void parseRecords(JSONArray jsonarray){
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                Record record = new Record();
                jsonobject = jsonarray.getJSONObject(i);
                record.setRid(jsonobject.getString("rid"));
                record.setLicenseID(jsonobject.getString("licenseID"));
                record.setLtype(jsonobject.getString("ltype"));
                record.setaType(jsonobject.getString("aType"));
                record.setaDate(jsonobject.getString("aDate"));
                record.setLtype(jsonobject.getString("ltype"));
                record.setaLocation(jsonobject.getString("aLocation"));
                recordsList.add(record);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initObjects();
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {
        recyclerViewRecords = (RecyclerView) findViewById(R.id.recycler);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
        beneficiaryRecyclerAdapter = new RecordsRecyclerAdapter(recordsList, activity);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRecords.setLayoutManager(mLayoutManager);
        recyclerViewRecords.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecords.setHasFixedSize(true);
        recyclerViewRecords.setAdapter(beneficiaryRecyclerAdapter);
        //databaseHelper = new DatabaseHelper(activity);

        //getDataFromSQLite();
    }});
    }


    /**
     * This method is to fetch all user records from SQLite
     */
   /* private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                recordsList.clear();
                recordsList.addAll(databaseHelper.getAllBeneficiary());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                beneficiaryRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
*/

   public void register(View view){
       Intent registerIntent = new Intent(RecordListActivity.this, RegisterActivity.class);
       startActivity(registerIntent);
   }



}
