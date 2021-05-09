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
import com.fypapplication.fypapp.databinding.FragmentSigninBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class SigninFragment extends Fragment {


    private static final String TAG = "SigninFragment";
    NavController navController;
    FragmentSigninBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();


    }

    private void actionViews() {


        binding.loginBtn.setOnClickListener(v -> {


            loginApi();
//            navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);

        });

        binding.forgotPassTv.setOnClickListener(v -> {

            navController.navigate(R.id.action_loginFragment_to_enterEmailFragment);
        });


    }

    private void loginApi() {


        JSONObject params = new JSONObject();

        try {
            // BODY
            params.put("email", binding.emailEt.getText().toString());
            params.put("password", binding.passwordEt.getText().toString());
//            Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
        } catch (Exception e) {
            Log.e(TAG, "_apiLogin: ", e);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "LoginAPI", params, res -> {

            Log.d(TAG, "_apiLogin: res " + res);

            JSONObject response = null;
            try {
                response = res.getJSONObject("user");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Login login = new Login();


            login.id = response.optString("id");
            login.name = response.optString("name");
            login.email = response.optString("email");
            login.phoneNumber = response.optLong("phone");
            login.type = response.optInt("type");

            login.lng = response.optLong("longitude");
            login.lat = response.optLong("latitude");


            //INITIALIZE SHARED PREFS TOKEN HERE USING login.data.accessToken.
            SharedPrefs sharedPrefs = new SharedPrefs(requireContext());
            try {
                sharedPrefs.setKey(res.getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sharedPrefs.putUser(login);
            navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);

        }, error -> {

            Log.e(TAG, "loginApi: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}