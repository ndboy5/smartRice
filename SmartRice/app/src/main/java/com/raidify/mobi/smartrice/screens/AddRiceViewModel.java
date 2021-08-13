package com.raidify.mobi.smartrice.screens;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.raidify.mobi.smartrice.model.RiceAsset;
import com.raidify.mobi.smartrice.server.APIServerSingleton;
import com.raidify.mobi.smartrice.utils.Constants;
import com.raidify.mobi.smartrice.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

public class AddRiceViewModel extends AndroidViewModel {
    RiceAsset newAsset = new RiceAsset();
    private Context context;
    SessionManager sessionManager;

    public AddRiceViewModel(@NonNull Application application) {
        super(application);
        context = application;
        sessionManager = new SessionManager(application);
    }

    public void addNewAsset(RiceAsset asset){
        this.newAsset = asset;
        sendRiceToServer(asset);
    }

    private void sendRiceToServer(RiceAsset asset){
        String url = Constants.urlBase + Constants.riceURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", asset.rice_id);
            jsonObject.put("Source_ID", asset.source_id);
            jsonObject.put("Unit_Price", asset.unitP);
            jsonObject.put("Size", asset.quantity);
            jsonObject.put("Owner", sessionManager.getUserId() ); //Loads the userID of the user currently logged on
            jsonObject.put("Rice_Type", asset.riceType);
            jsonObject.put("Creation_date", asset.creation_date);
            jsonObject.put("Last_update_date", asset.last_update_date);
            jsonObject.put("Batch_name", asset.batchName);
            jsonObject.put("State", asset.state);
            jsonObject.put("Status", asset.status);
            jsonObject.put("Farm_location", asset.farm_location);
            jsonObject.put("Source_ID", asset.source_id);
            jsonObject.put("Transaction_Status", "open"); //Default is open for all new harvest
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
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

    public boolean isAuthorisedToAddRice(){
       if(sessionManager.getUserId().substring(0,3).equals(Constants.FARMER_ID_PREFIX)) return true;
//        Log.i("ndboy", "substring=" + sessionManager.getUserId().substring(0,3));
        return false;
    }
}
