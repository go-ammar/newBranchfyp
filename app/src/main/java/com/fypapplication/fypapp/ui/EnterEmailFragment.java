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
import com.fypapplication.fypapp.databinding.FragmentEnterEmailBinding;
import com.fypapplication.fypapp.databinding.FragmentLoginBinding;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EnterEmailFragment extends Fragment {


    private static final String TAG = "Enter Email Fragment";
    private String URL;
    NavController navController;

    FragmentEnterEmailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_email, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actions();
    }

    private void actions() {


        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.setWebViewClient(new MyWebViewClient());

        binding.webView.loadUrl("www.nissanusa.com");

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


        binding.SubmitEmail.setOnClickListener(view -> {
            String email = binding.emailEt.getText().toString();

            if (email.length() < 6) {
                Log.d(TAG, "actions: email length " + email.length());
                Log.d(TAG, "actions: email " + email);
                binding.email.setError("Please fill correct Email");
            } else {
                Log.d(TAG, "actions: email length " + email.length());
                Log.d(TAG, "actions: email " + email);
                validations(email);
            }

            //TODO remove these lines
//            EnterEmailFragmentDirections.ActionEnterEmailFragmentToPinFragment actions = EnterEmailFragmentDirections.actionEnterEmailFragmentToPinFragment();
//            actions.setEmail(email);
//            navController.navigate(actions);

        });
    }

    private class MyWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            view.loadUrl("www.nissanusa.com");
            Log.d(TAG, "shouldOverrideUrlLoading: " + request.getUrl().toString());

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.d(TAG, "onPageFinished: " + url);
        }
    }

    private void validations(String email) {

        JSONObject params = new JSONObject();

        try {


            // BODY
            params.put("email", email);

        } catch (Exception e) {
            Log.e(TAG, "Enter Email Fragment: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.API_SEND_EMAIL + email, params, response -> {

            Log.d(TAG, "validation: res " + response);
            try {
                JSONArray jsonArray = response.getJSONArray("employees");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast toast = Toast.makeText(getContext(), "Incorrect email or password", Toast.LENGTH_SHORT);
//            toast.show();

            EnterEmailFragmentDirections.ActionEnterEmailFragmentToPinFragment actions = EnterEmailFragmentDirections.actionEnterEmailFragmentToPinFragment();
            actions.setEmail(email);
            navController.navigate(actions);

        }, error -> {

            Toast toast = Toast.makeText(getContext(), "Incorrect email", Toast.LENGTH_SHORT);
            toast.show();
            Log.d(TAG, "validation: error " + error);

        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


//        EnterEmailFragmentDirections.ActionEnterEmailFragmentToPinFragment actions = EnterEmailFragmentDirections.actionEnterEmailFragmentToPinFragment();
//        actions.setEmail(email);
//        navController.navigate(actions);

    }
}