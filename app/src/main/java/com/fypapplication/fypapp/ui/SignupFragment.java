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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentSignupBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

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
            params.put("latitude", 0);
            params.put("longitude", 0);
            params.put("type", binding.passwordEt.getText().toString());
            params.put("phone", binding.phoneEt.getText().toString());
//            Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
        } catch (Exception e) {
            Log.e(TAG, "signUpApi: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "SignupAPI", params, response -> {

            Log.d(TAG, "signUpApi: res " + response);

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

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}