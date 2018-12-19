package com.comp306.driving_record_api_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    //    private TextInputLayout textInputLayoutRid;
    private TextInputLayout textInputLayoutAType;
    private TextInputLayout textInputLayoutLType;
    private TextInputLayout textInputLayoutDate;
    private TextInputLayout textInputLayoutLicenseId;
    private TextInputLayout textInputLayoutLocation;

    private TextInputEditText textInputEditTextLicenseID;
    private TextInputEditText textInputEditTextLicenseType;
    private TextInputEditText textInputEditTextAType;
    private TextInputEditText textInputEditTextDate;
    private TextInputEditText textInputEditTextRid;
    private TextInputEditText textInputEditTextLocation;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewBenefList;

    private Record record;
    private DrivingRecordAPIClient client;
    private final static String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initObjects();
        initListeners();
    }

    //Initialize Views
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutAType = (TextInputLayout) findViewById(R.id.textInputLayoutAType);
        textInputLayoutLType = (TextInputLayout) findViewById(R.id.textInputLayoutLType);
        textInputLayoutDate = (TextInputLayout) findViewById(R.id.textInputLayoutDate);
        textInputLayoutLicenseId = (TextInputLayout) findViewById(R.id.textInputLayoutLicenseId);
        textInputLayoutLocation = (TextInputLayout) findViewById(R.id.textInputLayoutLocation);

        textInputEditTextLicenseID = (TextInputEditText) findViewById(R.id.textInputEditTextLicenseId);
        textInputEditTextLicenseType = (TextInputEditText) findViewById(R.id.textInputEditTextLType);
        textInputEditTextAType = (TextInputEditText) findViewById(R.id.textInputEditTextAType);
        textInputEditTextDate = (TextInputEditText) findViewById(R.id.textInputEditTextDate);
        textInputEditTextLocation = (TextInputEditText) findViewById(R.id.textInputEditTextLocation);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewBenefList = (AppCompatTextView) findViewById(R.id.appCompatTextViewBenefList);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewBenefList.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        client = DrivingRecordAPIClient.getInstance();
        record = new Record();
    }

    @Override
    public void onClick(View v) {
        //post method with the Record object
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("rid", UUID.randomUUID().toString().substring(0, 4));
            jsonParams.put("licenseID", textInputEditTextLicenseID.getText().toString().trim());
            jsonParams.put("ltype", textInputEditTextLicenseType.getText().toString().trim());
            jsonParams.put("aType", textInputEditTextAType.getText().toString().trim());
            jsonParams.put("aDate", textInputEditTextDate.getText().toString().trim());
            jsonParams.put("aLocation", textInputEditTextLocation.getText().toString().trim());
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.postData(getApplicationContext(), entity, new JsonHttpResponseHandler() {
            /**
             * Returns when request succeeds
             *
             * @param statusCode http response status line
             * @param headers    response headers if any
             * @param response   parsed response if any
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, response.toString());
                Intent accountsIntent = new Intent(activity, RecordListActivity.class);
                startActivity(accountsIntent);
                finish();
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
                Log.e(TAG, errorResponse.toString());
            }

        });
    }

}
