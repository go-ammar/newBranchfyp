package com.fypapplication.fypapp.ui;

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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentSignupBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends Fragment {

    private static final String TAG = "SignupFragment";
    FragmentSignupBinding binding;
    NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);


        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        actionViews();


    }

    private void actionViews() {

        binding.signUpBtn.setOnClickListener(v -> {

//            navController.navigate(R.id.action_loginFragment_to_enterEmailFragment);

            signUpApi();
        });
    }

    private void signUpApi() {
        JSONObject params = new JSONObject();

        try {
            // BODY
            params.put("email", binding.emailEt.getText().toString());
            params.put("password", binding.passwordEt.getText().toString());
            params.put("latitude", 1);
            params.put("longitude", 1);
            params.put("name", binding.nameEt.getText().toString());
            params.put("type", 2);
            params.put("phone", binding.phoneEt.getText().toString());
            Log.d(TAG, "signUpApi: "+params);
//            Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
        } catch (Exception e) {
            Log.e(TAG, "signUpApi: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_SIGNUP, params, response -> {

            Log.d(TAG, "signUpApi: res " + response);
            Toast.makeText(getContext(), "User Created", Toast.LENGTH_SHORT).show();

//            Login login = new Login();
//
//            login.name = response.optString("name");
//            login.email = response.optString("email");
//            login.phoneNumber = response.optLong("phone");
//            login.type = Global.CUSTOMER_TYPE;
//
//            login.lng = response.optLong("longitude");
//            login.lat = response.optLong("latitude");


        }, error -> {

            Log.e(TAG, "signUpApi: ", error);

        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}