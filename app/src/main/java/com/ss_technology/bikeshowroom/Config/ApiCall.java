package com.ss_technology.bikeshowroom.Config;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ApiCall {

    private Context context;
   Loading_Dai alert;

    public ApiCall(Context context)
    {
        this.context=context;
        alert=new Loading_Dai(context);
    }

    public String Insert(final HashMap<String, String> map, String pageName, final VolleyCallback callback)
    {
        alert.Show();
        StringRequest sr = new StringRequest(Request.Method.POST, BaseURL.Path()+pageName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               callback.onSuccess(response);
               alert.Hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
                alert.Hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = map;
                return hashMap;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sr);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        return "";
    }


    public String get(String pageName, final VolleyCallback callback)
    {
        alert.Show();
        StringRequest sr = new StringRequest(Request.Method.POST, BaseURL.Path()+pageName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
                alert.Hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
               alert.Hide();
            }
        });
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sr);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
        return "";
    }

    public String getWithOutAlert(String pageName, final VolleyCallback callback)
    {
        StringRequest sr = new StringRequest(Request.Method.POST, BaseURL.Path()+pageName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
            }
        });
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sr);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
        return "";
    }

    public String Insert2(final HashMap<String, String> map, String pageName, final VolleyCallback callback)
    {
        StringRequest sr = new StringRequest(Request.Method.POST, BaseURL.Path()+pageName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = map;
                return hashMap;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sr);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        return "";
    }


}
