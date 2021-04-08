package com.fypapplication.fypapp.ui;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.FragmentLoginBinding;
import com.fypapplication.fypapp.databinding.FragmentPinBinding;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;

import org.json.JSONObject;


public class PinFragment extends Fragment {

    private static final String TAG = "PinFragment";
    private String URL;
    NavController navController;

    FragmentPinBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pin, container, false);
        return binding.getRoot();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }


    private void actionViews() {


        binding.SubmitPin.setOnClickListener(view -> {
            //TODO add curly brackets
            if (validation()){
                Toast.makeText(requireContext(), "Pin Submitted", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_pinFragment_to_forgotpass);
            }


        });

    }


    private boolean validation() {


        if (binding.pinEt.getText().toString().length() < 6) {
            binding.pin.setError("Please enter 6 digit pin");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            JSONObject params = new JSONObject();

            PinFragmentArgs email = PinFragmentArgs.fromBundle(getArguments());

            try {

                // BODY
                params.put("email", email.getEmail());
                params.put("pin", binding.pinEt.getText().toString());


            } catch (Exception e) {
                Log.e(TAG, "validation: ", e);
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.API_PIN, params, response -> {

                Log.d(TAG, "validation: res " + response);

                progressDialog.dismiss();

                PinFragmentDirections.ActionPinFragmentToForgotpass action = PinFragmentDirections.actionPinFragmentToForgotpass();
                action.setEmail(email.getEmail());
                navController.navigate(action);


            }, error -> {

                progressDialog.dismiss();
                Toast toast = Toast.makeText(getContext(), "Incorrect pin", Toast.LENGTH_SHORT);
                toast.show();
                Log.d(TAG, "validation: error " + error);

            });

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        }


        return false;


    }
}