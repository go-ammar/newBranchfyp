package com.fypapplication.fypapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentLoginBinding;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {


    private static final String TAG = "LoginFragment";
    NavController navController;
    FragmentLoginBinding binding;
    private String URL;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        try {
            actionViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void actionViews() throws JSONException {


        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setupViewPager(binding.viewPager);

//        binding.loginBtn.setOnClickListener(v -> {
//            validations();
//
//
////                navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);
//
//        });
//        binding.forgotPassTv.setOnClickListener(v -> {
//
//            navController.navigate(R.id.action_loginFragment_to_enterEmailFragment);
//
//        });


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SigninFragment(), "Sign in");
        adapter.addFragment(new SignupFragment(), "Sign up");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
            //return null;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
        }

    }


//    private boolean validations() {
//
//
//        if (binding.emailEt.getText() != null && !binding.emailEt.getText().toString().isEmpty()) {
//            if (binding.passwordEt.getText() != null && !binding.passwordEt.getText().toString().isEmpty()) {
//                ProgressDialog progressDialog = new ProgressDialog(getContext());
//                progressDialog.setMessage("Please wait...");
//                progressDialog.show();
//
//                JSONObject params = new JSONObject();
//
//                try {
//
//                    // BODY
//                    params.put("email", binding.emailEt.getText().toString());
//                    params.put("password", binding.passwordEt.getText().toString());
//                    //        Log.d(TAG, "validations: email is:  " + binding.emailEt.getText().toString());
//                } catch (Exception e) {
//                    Log.e(TAG, "_apiLogin: ", e);
//                }
//
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_LOGIN, params, response -> {
//
//                    Log.d(TAG, "_apiLogin: res " + response);
//
//                    Moshi moshi = new Moshi.Builder().build();
//                    JsonAdapter<Login> jsonAdapter = moshi.adapter(Login.class);
//
//                    String message = response.toString();
//
//                    Login login = new Login();
//                    try {
//                        login = jsonAdapter.fromJson(message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //INITIALIZE SHARED PREFS TOKEN HERE USING login.data.accessToken.
//                    Log.d(TAG, "validations: token set with new class " + login.data.accessToken);
//                    SharedPrefs sharedPrefs = new SharedPrefs(requireContext());
//                    sharedPrefs.setKey(login.data.accessToken);
//
////                    sharedPrefs.putUser(login, "user");
//
////                    Log.d(TAG, "validations: " + sharedPrefs.getUser("user").data.id);
//
//
//                    progressDialog.dismiss();
//
//                    Intent intent = new Intent(requireContext(), HomeLandingActivity.class);
//                    startActivity(intent);
////                    navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);
//
//
//                }, error -> {
//
//                    Log.d(TAG, "validations: " + WebServices.API_LOGIN + " params: " + binding.emailEt.getText().toString() +
//                            " " + binding.passwordEt.getText().toString());
//                    progressDialog.dismiss();
//                    Toast toast = Toast.makeText(getContext(), "Incorrect email or password", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Log.d(TAG, "_apiLogin: error " + error);
//
//
//                });
//
//                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
//
//            } else
//                binding.textInputLayout.setError(getString(R.string.error_empty_fields));
//
//        } else
//            binding.textEmail.setError(getString(R.string.error_empty_fields));
//
//        return false;
//    }


}
