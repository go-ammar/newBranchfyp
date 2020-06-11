package com.electrosoft.electrosoftnew.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentForgotpassBinding;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassFragment extends Fragment {
    private String URL;
    private static final String TAG = "ForgotPassFragment";
    NavController navController;
    FragmentForgotpassBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate( inflater,R.layout.fragment_forgotpass, container, false );
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews(){
        binding.ChangePasswordbtn.setOnClickListener( v -> {
            navController.navigate(R.id.action_forgotpass_to_loginFragment);
        });
    }


    private void Insertrooms() {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                Toast.makeText(requireContext(), "Sucessfully Change password", Toast.LENGTH_SHORT).show();
                //signin();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error+" Try Again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("email",binding.emailEt.getText().toString());
                params.put("newPwd",binding.PasswordET.getText().toString());


                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);


    }

    private boolean validations(){

        if(binding.emailEt.getText()!=null && !binding.emailEt.getText().toString().isEmpty()){
            if(binding.PasswordET.getText()!=null && !binding.PasswordET.getText().toString().isEmpty()) {
                return true;
            }
            else
                binding.acount.setError(getString(R.string.error_empty_fields));

        }else
            binding.Email.setError(getString(R.string.error_empty_fields));

        return false;
    }



}
