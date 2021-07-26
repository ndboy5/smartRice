package com.raidify.mobi.smartrice.server;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*  This class uses volley to manage communication with the Rest API Server.
    It is a Singleton class
    TODO: Try out non static methods in this Singleton instance
 */
public class APIServerSingleton {

    private static APIServerSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    //private constructor for singleton instance
    private APIServerSingleton(Context context) {
        //NOTE: It is recommended that the singleton class be instantiated with application context (not activity)
        ctx = context;
        requestQueue = getRequestQueue();

                };

    public static synchronized APIServerSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new APIServerSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public  void cancelPendingRequests(Object tag){
        if(requestQueue!= null){
            requestQueue.cancelAll(tag);
        }
    }

}
