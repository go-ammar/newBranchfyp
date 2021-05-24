package com.fypapplication.fypapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.databinding.ActivityHomeLandingBinding;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.helper.Global;
import com.fypapplication.fypapp.helper.NotifyJobService;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeLandingActivity extends AppCompatActivity {

    private static final String TAG = "HomeLandingActivity";
    private final String EXTRA_LOGOUT = "clearBackStack";
    NavController navController;
    ActivityHomeLandingBinding binding;
    SharedPrefs sharedPrefs;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);


        if (getIntent().getBooleanExtra(EXTRA_LOGOUT, false)) {
            clearBackStack();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_home_landing);
            setUpNavigation();
        }
        NotifyJobService.schedule(this, NotifyJobService.ONE_DAY_INTERVAL);

        sharedPrefs = new SharedPrefs(getApplicationContext());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.logoutBtn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpNavigation() {

        setSupportActionBar(binding.layout.toolbar);


        appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.nav_dashboard,
                        R.id.nav_notifications,
                        R.id.nav_contact,
                        R.id.nav_about)
                        .setOpenableLayout(binding.drawerLayout)
                        .build();


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void clearBackStack() {

        Intent intent = new Intent(this, HomeLandingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();

    }

    public boolean logout(MenuItem menuItem) {

        DeleteRoomDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.delete_room_dialog,
                null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(binding1.getRoot())
                .create();
        binding1.text.setText(R.string.logout_text);

        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding1.cancelButton.setText("No");
        binding1.cancelButton.setOnClickListener(view -> {
            menuItem.setChecked(false);

            dialog.dismiss();
        });

        binding1.confirmButton.setText("Yes");
        binding1.confirmButton.setOnClickListener(v -> {

            if (sharedPrefs.getUser().type == Global.MECH_TYPE) {
                deleteDeviceTokenApi();
            }

            sharedPrefs.clearSpecificKey("key");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        return true;
    }

    private void deleteDeviceTokenApi() {

        String id = sharedPrefs.getUser().id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, WebServices.API_DELETE_DEVICE_TOKEN_BY_USER + id, null, res -> {

            Log.d(TAG, "deleteDeviceTokenApi: " + res);

        }, error -> {

            Log.e(TAG, "deleteDeviceTokenApi: ", error);

        }) {
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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }


}