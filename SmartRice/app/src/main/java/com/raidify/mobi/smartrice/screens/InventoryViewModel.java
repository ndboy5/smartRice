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

public class InventoryViewModel extends AndroidViewModel {
    MutableLiveData<JSONArray> riceAssetList;
    Context context;

    public InventoryViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public MutableLiveData<JSONArray> getRiceAssetList(){
        if(riceAssetList==null) riceAssetList = new MutableLiveData<>();
        return this.riceAssetList;
    }

    public void getOpenRiceAssets(){
        String url = Constants.urlBase + Constants.riceURI+ "/open";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        try {
                            riceAssetList.postValue(response.getJSONArray("data"));
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

    public void getAllRiceAssetsOwnedByUser(String owner){
        String url = Constants.urlBase + Constants.riceURI+ "/owner/" + owner;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        try {
                            riceAssetList.postValue(response.getJSONArray("data"));
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