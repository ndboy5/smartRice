package com.raidify.mobi.smartrice.utils;

import com.android.volley.Request;

public class Constants {
    public static final int   RANDOM_NUM_UPPER_BOUND=1000000;
    public static final String RICE_ID_PREFIX = "RCE";
    public static final String FARMER_ID_PREFIX = "FMR";
    public static final String PROCESSOR_ID_PREFIX = "PRC";
    public static final String WHOLESALER_ID_PREFIX = "WHS";
    public static final String RETAILER_ID_PREFIX = "RET";
    public static final String CONSUMER_ID_PREFIX = "CON";

    //use final variable for the Volley actions
    public static final int ACTION_GET = Request.Method.GET;
    public static final int ACTION_POST = Request.Method.POST;
    public static final int ACTION_PATCH = Request.Method.PATCH;
    public static final int ACTION_PUT = Request.Method.PUT;
    public static final int ACTON_DELETE = Request.Method.DELETE;

    // variables for building URLs
    public static final int port = 5000;
    public static final String urlBase = "http://143.198.164.186:" + Integer.toString(port) +"/api/v1/";
//    public static final String urlBase = "http://172.20.10.2:" + Integer.toString(port) +"/api/v1/";
//    public static final String urlBase = "http://172.20.10.4:" + Integer.toString(port) +"/api/v1/";
    public static final String accountURI = "account";
    public static final String riceURI = "rice";
    public static final String checkURI = "rice/check";
    public static final String inventory = "rice/inventory";
}
