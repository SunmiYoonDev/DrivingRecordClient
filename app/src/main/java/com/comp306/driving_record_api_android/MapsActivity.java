package com.comp306.driving_record_api_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DrivingRecordAPIClient histClient;
    private GoogleMap mMap;
    private final static String TAG = MapsActivity.class.getSimpleName();
    private boolean useRFC5179CompatibilityMode = true;
    private String lat = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        histClient = DrivingRecordAPIClient.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            lat = intent.getStringExtra("lat");
            longitude = intent.getStringExtra("long");
        }
        Log.e(TAG, "res: ");
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Loop and parse through the response to get our data
                    JSONArray array = response.getJSONArray("features");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject record = array.getJSONObject(i).getJSONObject("attributes");
                        Log.e(TAG, "Occurrence Date: %s %s, %s" +
                                record.getString("rid") +
                                record.getString("licenseID") +
                                record.getString("ltype"));
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
                String jsonString = getResponseString(responseBody, getCharset());
                if (jsonString != null) {
                    jsonString = jsonString.trim();
                    if (useRFC5179CompatibilityMode) {
                        if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
                            result = new JSONTokener(jsonString).nextValue();
                        }
                    } else {
                        if ((jsonString.startsWith("{") && jsonString.endsWith("}"))
                                || jsonString.startsWith("[") && jsonString.endsWith("]")) {
                            result = new JSONTokener(jsonString).nextValue();
                        } else if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                            result = jsonString.substring(1, jsonString.length() - 1);
                        }
                    }
                }
                if (result == null) {
                    result = jsonString;
                }
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
        histClient.getData(handler);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.valueOf(lat), Double.valueOf(longitude));
        mMap.addMarker(new MarkerOptions().position(sydney)/*.title("Marker in Sydney")*/);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));
    }
}
