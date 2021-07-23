package com.raidify.mobi.smartrice.screens;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.raidify.mobi.smartrice.model.Asset;
import com.raidify.mobi.smartrice.server.APIServerSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRiceViewModel extends AndroidViewModel {
    Asset newAsset = new Asset();
    private Context context;

    public AddRiceViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public void addNewAsset(Asset asset){
        this.newAsset = asset;
        sendAssetToServer(asset);
    }

    private void sendAssetToServer(Asset asset){
        String url = APIServerSingleton.urlBase + APIServerSingleton.riceURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", asset.rice_id);
            jsonObject.put("Source_ID", asset.source_id);
            jsonObject.put("Size", asset.quantity);
            jsonObject.put("Owner", asset.owner);
            jsonObject.put("Type", asset.riceType);
            jsonObject.put("Creation_date", asset.creation_date);
            jsonObject.put("Last_update_date", asset.last_update_date);
            jsonObject.put("Batch_name", asset.batchName);
            jsonObject.put("State", asset.state);
            jsonObject.put("Status", asset.status);
            jsonObject.put("Farm_location", asset.farm_location);
            jsonObject.put("Source_ID", asset.source_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
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

        APIServerSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
}


}
