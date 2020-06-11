package com.electrosoft.electrosoftnew.ui;

import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {


    private static final String TAG = "LoginFragment";
    private String URL;
    NavController navController;

    FragmentLoginBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        return binding.getRoot();


    }
    // CTRL + o

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews(){


        binding.loginBtn.setOnClickListener(v -> {
//            if(validations())
//            Toast.makeText(requireContext(), "Login btn clicked", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_loginFragment_to_homeLandingActivity);

        });
        binding.forgotPassTv.setOnClickListener( v -> {

            navController.navigate(R.id.action_loginFragment_to_forgotpass);
        });

        

    }


    private void Login()
    {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                if (response.equals("0"))
                {

                    Toast.makeText(requireContext(), response+"sorry", Toast.LENGTH_SHORT).show();
                }
                else {
                    String b = response;
                    String[] parts = b.split( "&" );
                    String part1 = parts[0];
                    String part2 = parts[1];
                   // SharedPreferences sharedPreferences = getSharedPreferences( "mypreference", MODE_PRIVATE );
                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    //editor.putString( "value", part2 );
                    //editor.apply();

                    if (part1.equals("1"))
                    {
                        //loginl();
                        Toast.makeText(requireContext(), response+"", Toast.LENGTH_SHORT).show();
                    }
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), error + "sorry", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", binding.emailEt.getText().toString());
                params.put("pass", binding.passwordEt.getText().toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }



    private boolean validations(){

        if(binding.emailEt.getText()!=null && !binding.emailEt.getText().toString().isEmpty()){
        if(binding.passwordEt.getText()!=null && !binding.passwordEt.getText().toString().isEmpty()) {
            return true;
        }else
            binding.textInputLayout.setError(getString(R.string.error_empty_fields));

        }else
            binding.textEmail.setError(getString(R.string.error_empty_fields));

        return false;
    }

}
