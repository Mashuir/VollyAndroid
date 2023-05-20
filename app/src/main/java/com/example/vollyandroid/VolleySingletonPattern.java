package com.example.vollyandroid;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingletonPattern {

    private static VolleySingletonPattern mInstance;
    private RequestQueue requestQueue;
    private final Context mContext;

    private VolleySingletonPattern(Context context) {
        mContext = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingletonPattern getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingletonPattern(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


}
