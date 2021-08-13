package com.raidify.mobi.smartrice.screens;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.raidify.mobi.smartrice.model.RiceAsset;
import com.raidify.mobi.smartrice.server.APIServerSingleton;
import com.raidify.mobi.smartrice.utils.Constants;
import com.raidify.mobi.smartrice.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class TradingViewModel extends AndroidViewModel {
    Context context;
    SessionManager sessionManager;
    MutableLiveData<JSONObject> riceDetail ;
    public TradingViewModel(@NonNull Application application) {
        super(application);
       context = application;
       sessionManager = new SessionManager(context);
       riceDetail = new MutableLiveData<>();
    }

    public MutableLiveData<JSONObject> getRiceDetail() {
        if (riceDetail == null) {
            riceDetail = new MutableLiveData<JSONObject>();
        }
        return this.riceDetail;
    }

    public void getRiceDetailById(String rice_id) {
        String url = Constants.urlBase + Constants.riceURI + "/check";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", rice_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        try {
                            riceDetail.postValue(response.getJSONObject("data"));
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

    public void sellRice(){
        String url = Constants.urlBase + Constants.riceURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",this.riceDetail.getValue().getString("ID") );
            jsonObject.put("Source_ID", this.riceDetail.getValue().getString("Source_ID"));
            jsonObject.put("Size", this.riceDetail.getValue().getString("Size"));
            jsonObject.put("Last_owner", this.riceDetail.getValue().getString("Last_owner"));
            jsonObject.put("Owner", this.riceDetail.getValue().getString("Owner")); //TODO: Owner ID should come from the session manager
            jsonObject.put("Rice_Type", this.riceDetail.getValue().getString("Rice_Type"));
            jsonObject.put("Creation_date", this.riceDetail.getValue().getString("Creation_date"));
            jsonObject.put("Last_update_date", new Date().getTime());
            jsonObject.put("Batch_name", this.riceDetail.getValue().getString("Batch_name"));
            jsonObject.put("State", this.riceDetail.getValue().getString("State"));
            jsonObject.put("Status", this.riceDetail.getValue().getString("Status"));
            jsonObject.put("Farm_location", this.riceDetail.getValue().getString("Farm_location"));
            jsonObject.put("Transaction_Status", "open");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        riceDetail.postValue(response);
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

    public void releaseRice(){

        String url = Constants.urlBase + Constants.riceURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",this.riceDetail.getValue().getString("ID") );
            jsonObject.put("Source_ID", this.riceDetail.getValue().getString("Source_ID"));
            jsonObject.put("Size", this.riceDetail.getValue().getString("Size"));
            jsonObject.put("Last_owner", this.riceDetail.getValue().getString("Last_owner"));
            jsonObject.put("Owner", this.riceDetail.getValue().getString("Owner")); //TODO: Owner ID should come from the session manager
            jsonObject.put("Rice_Type", this.riceDetail.getValue().getString("Rice_Type"));
            jsonObject.put("Creation_date", this.riceDetail.getValue().getString("Creation_date"));
            jsonObject.put("Last_update_date", new Date().getTime());
            jsonObject.put("Batch_name", this.riceDetail.getValue().getString("Batch_name"));
            jsonObject.put("State", this.riceDetail.getValue().getString("State"));
            jsonObject.put("Status", this.riceDetail.getValue().getString("Status"));
            jsonObject.put("Farm_location", this.riceDetail.getValue().getString("Farm_location"));
            jsonObject.put("Transaction_Status", "closed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        riceDetail.postValue(response);
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

    /**
     * To buy a portion and not the entire Asset quantity
     * @param newOwnerId
     * @param portion
     */
    public void buyRicePortion(String newAssetID, String newOwnerId, Double portion){
        String url = Constants.urlBase + Constants.riceURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",newAssetID );
            jsonObject.put("Source_ID", this.riceDetail.getValue().getString("ID"));
            jsonObject.put("Size", portion);
            jsonObject.put("Last_owner", this.riceDetail.getValue().getString("Owner"));
            jsonObject.put("Owner", newOwnerId); //TODO: Owner ID should come from the session manager
            jsonObject.put("Rice_Type", this.riceDetail.getValue().getString("Rice_Type"));
            jsonObject.put("Creation_date", new Date().getTime());
            jsonObject.put("Last_update_date", new Date().getTime());
            jsonObject.put("Batch_name", this.riceDetail.getValue().getString("Batch_name"));
            jsonObject.put("State", this.riceDetail.getValue().getString("State"));
            jsonObject.put("Status", this.riceDetail.getValue().getString("Status"));
            jsonObject.put("Farm_location", this.riceDetail.getValue().getString("Farm_Location"));
            jsonObject.put("Transaction_Status", "pending");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        riceDetail.postValue(response);
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

    public void buyRiceAll(String ownerId){
        try {
            transferRiceOwnership(this.riceDetail.getValue().getString("Owner"), sessionManager.getUserId(), "pending");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void decreaseQuantityOfSourceAsset(RiceAsset riceAsset, Double newQuantity){
        String url = Constants.urlBase + Constants.riceURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",riceAsset.rice_id);
            jsonObject.put("Source_ID",riceAsset.source_id);
            jsonObject.put("Size", newQuantity);
            jsonObject.put("Last_owner", riceAsset.last_owner);
            jsonObject.put("Owner", riceAsset.owner); //TODO: Owner ID should come from the session manager
            jsonObject.put("Rice_Type", riceAsset.riceType);
            jsonObject.put("Creation_date", riceAsset.creation_date);
            jsonObject.put("Last_update_date", new Date().getTime());
            jsonObject.put("Batch_name", riceAsset.batchName);
            jsonObject.put("State", riceAsset.state);
            jsonObject.put("Status", riceAsset.status);
            jsonObject.put("Farm_location", riceAsset.farm_location);
            jsonObject.put("Transaction_Status", riceAsset.trans_status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        riceDetail.postValue(response);
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

    //To change the ownership of a given Rice asset
    private void transferRiceOwnership(String formerOwner, String newOwner, String transaction_status){

        String url = Constants.urlBase + Constants.riceURI + "/transfer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", this.riceDetail.getValue().getString("ID"));
            jsonObject.put("newOwner", newOwner);
            jsonObject.put("lastOwner", formerOwner);
            jsonObject.put("updateDate", new Date().getTime());
            jsonObject.put("transaction_status", transaction_status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ACTION_PATCH, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ndboy", "SERVER RESPONSE: " + response.toString());
                        riceDetail.postValue(response);
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