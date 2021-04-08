package com.fypapplication.fypapp.ui;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentForgotpassBinding;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONObject;

import java.util.Objects;

public class ForgotPassFragment extends Fragment {
    private static final String TAG = "ForgotPassFragment";
    NavController navController;
    FragmentForgotpassBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgotpass, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews() {

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAllowFileAccess(true);
//        binding.webView.setWebViewClient(new MyWebViewClient());

        binding.webView.loadUrl("https://www.google.com");

        binding.ChangePasswordbtn.setOnClickListener(v -> {

            //TODO remove next line
//            navController.navigate(R.id.action_forgotpass_to_loginFragment);
            //TODO ask about password details from someone
//            if (binding.PasswordET.equals(binding.CPasswordET) && binding.PasswordET.getText().toString().length() > 5) {
//
//
//                JSONObject params = new JSONObject();
//
//                PinFragmentArgs email = PinFragmentArgs.fromBundle(getArguments());
//
//                try {
//
//
//                    // BODY
//                    params.put("email", email.getEmail());
//                    params.put("password", binding.CPasswordET.toString());
//
//
//                } catch (Exception e) {
//                    Log.e(TAG, "validation: ", e);
//                }
//
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_CHANGE_PASSWORD, params, response -> {
//
//                    Log.d(TAG, "validation: res " + response);
//                    navController.navigate(R.id.action_forgotpass_to_loginFragment);
//
//                }, error -> {
//
//                    Toast toast = Toast.makeText(getContext(), "Incorrect pin", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Log.d(TAG, "validation: error " + error);
//
//                });
//
//                VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
//
//
//            } else {
//                binding.CPassw.setError("Passwords to not match");
//
//            }
//
//
//        });
        });

        binding.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return url.endsWith(".pdf");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // DO NOT CALL SUPER METHOD
//                super.onReceivedSslError(view, handler, error);
                Log.d(TAG, "onReceivedSslError: error " + error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Make a note about the failed load.
                Log.d(TAG, "onReceivedError: description " + description);
                Log.d(TAG, "onReceivedError: view  " + view);
            }


        });
//
//    private void Insertrooms() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("Response", response);
//                Toast.makeText(requireContext(), "Sucessfully Change password", Toast.LENGTH_SHORT).show();
//                //signin();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(requireContext(), error + " Try Again", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<String, String>();
//                //         params.put("email",binding.emailEt.getText().toString());
//                params.put("newPwd", binding.PasswordET.getText().toString());
//
//
//                return params;
//            }
//
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//        requestQueue.add(stringRequest);
//
//
//    }

//    private boolean validations(){
//
//        if(binding.emailEt.getText()!=null && !binding.emailEt.getText().toString().isEmpty()){
//            if(binding.PasswordET.getText()!=null && !binding.PasswordET.getText().toString().isEmpty()) {
//                return true;
//            }
//            else
//                binding.acount.setError(getString(R.string.error_empty_fields));
//
//        }else
//            binding.Email.setError(getString(R.string.error_empty_fields));
//
//        return false;
//    }
//


    }

//    private class MyWebViewClient extends WebViewClient {
//
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//            view.loadUrl("https://www.google.com");
//            Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl().toString());
//
//            return true;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//
//            Log.d(TAG, "onPageFinished: " + url);
//        }
//    }

}

