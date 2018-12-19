package com.comp306.driving_record_api_android;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;

public class DrivingRecordAPIClient {
    private static final DrivingRecordAPIClient ourInstance = new DrivingRecordAPIClient();
    //Initialize a client
    private final AsyncHttpClient client = new AsyncHttpClient();
    public static RequestParams params;
    //Base URI that works with the apigee proxy
    public final String BASE_URL = "http://qoh1234-eval-test.apigee.net/proxy4drivingrecordapi/api/Records";
    private final String API_GET = "/allrecords";
    public static final String API_POST = "?";
    public static final String API_DELETE = "/";

    public static DrivingRecordAPIClient getInstance() {
        return ourInstance;
    }

    public void getData(JsonHttpResponseHandler handler) {
        //This is where we sign the API key to secure and protect our API, however this still could be enhanced
        //by sending the api key to the header
        RequestParams params = new RequestParams();
        params.add("apikey", "aenp1BjFkitXqXcqEn6WGkAL9QAAquJO");
        client.get(BASE_URL + API_GET, params, handler);
    }

    public void postData(Context context, HttpEntity httpParams, JsonHttpResponseHandler handler) {
        //This is where we sign the API key to secure and protect our API, however this still could be enhanced
        //by sending the api key to the header
        RequestParams params = new RequestParams();
        params.add("apikey", "aenp1BjFkitXqXcqEn6WGkAL9QAAquJO");
        client.post(context, BASE_URL + API_POST + "apikey=aenp1BjFkitXqXcqEn6WGkAL9QAAquJO", httpParams, "application/json", handler);
    }

    public void deleteData(String deleteMethod, JsonHttpResponseHandler handler) {
        //This is where we sign the API key to secure and protect our API, however this still could be enhanced
        //by sending the api key to the header
        RequestParams params = new RequestParams();
        params.add("apikey", "aenp1BjFkitXqXcqEn6WGkAL9QAAquJO");
        client.delete(BASE_URL + API_DELETE + deleteMethod, params, handler);
    }
}
