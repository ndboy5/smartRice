package com.raidify.mobi.smartrice.screens;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.raidify.mobi.smartrice.model.AccountAsset;
import com.raidify.mobi.smartrice.server.APIServerSingleton;
import com.raidify.mobi.smartrice.utils.Constants;
import com.raidify.mobi.smartrice.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginViewModel extends AndroidViewModel {
    SessionManager sessionManager;
    MutableLiveData<JSONObject> userDetails;
    Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application;
        sessionManager= new SessionManager(context);
    }

    public boolean isLoggedIn(){
        return sessionManager.isLogin();

    }



    public MutableLiveData<JSONObject> getAccountDetail() {
        if (userDetails == null) {
            userDetails = new MutableLiveData<JSONObject>();
        }
        return this.userDetails;
    }

    public void getAccountDataFromServer(String id, String passPhrase){
        String url = Constants.urlBase + Constants.accountURI + "/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", id);
            jsonObject.put("passphrase", passPhrase);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        try {
                            userDetails.postValue(response.getJSONObject("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ndboy", "SERVER ERROR: " + error.toString());
                    }
                });
        //add request to the request queue
        APIServerSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void logoutAccount(){
        sessionManager.logoutUser();
    }

    public String getSessionName(){
        return sessionManager.getUserDetails().get("name");
    }
}