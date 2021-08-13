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
import com.raidify.mobi.smartrice.server.APIServerSingleton;
import com.raidify.mobi.smartrice.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckViewModel extends AndroidViewModel {
    MutableLiveData<JSONArray> riceHistoryData;
    Context context;

    public CheckViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }


    public MutableLiveData<JSONArray> getRiceHistoryData(){
        if(riceHistoryData==null) riceHistoryData = new MutableLiveData<>();
        return this.riceHistoryData;
    }

    public void getRiceHistoryFromBlockchain(String riceId){
        String url = Constants.urlBase + Constants.riceURI+ "/track/" + riceId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        try {
                            riceHistoryData.postValue(response.getJSONArray("data"));
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
}