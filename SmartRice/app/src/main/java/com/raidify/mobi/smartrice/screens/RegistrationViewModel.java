package com.raidify.mobi.smartrice.screens;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.raidify.mobi.smartrice.model.AccountAsset;
import com.raidify.mobi.smartrice.server.APIServerSingleton;
import com.raidify.mobi.smartrice.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationViewModel extends AndroidViewModel {
    Context context;
    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    //This method takes the newly created account and sends to network
    public void sendAccountDetailsToNetwork(AccountAsset accountAsset){
        String url = Constants.urlBase + Constants.accountURI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", accountAsset.ID);
            jsonObject.put("Name", accountAsset.firstName);
            jsonObject.put("Midname", accountAsset.midName);
            jsonObject.put("Surname", accountAsset.surname);
            jsonObject.put("Role", accountAsset.role);
            jsonObject.put("Pass_phrase", accountAsset.pass_phrase);
            jsonObject.put("Location", accountAsset.location);
            jsonObject.put("Creation_date", accountAsset.CreationDate);
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
}
