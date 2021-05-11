package com.fypapplication.fypapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.adapters.MechanicPriceAdapter;
import com.fypapplication.fypapp.databinding.DialogueMechanicServiceBinding;
import com.fypapplication.fypapp.databinding.ItemMechServicesBinding;
import com.fypapplication.fypapp.databinding.DeleteRoomDialogBinding;
import com.fypapplication.fypapp.models.MechServices;
import com.fypapplication.fypapp.models.User;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.fypapplication.fypapp.webservices.VolleySingleton;
import com.fypapplication.fypapp.webservices.WebServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements MechanicPriceAdapter.MyServicesInterface {

    private static final String TAG = "MapsFragment";
    ArrayList<User> userArrayList;
    ArrayList<MechServices> mechServicesArrayList;
    ArrayList<MechServices> priceArrayList;
    Context context;
    String mechId;
    MechanicPriceAdapter mechanicPriceAdapter;
    ItemMechServicesBinding binding;
    DialogueMechanicServiceBinding dialogueMechanicServiceBinding;
    Dialog dialog;
    String customerId;
    MechanicPriceAdapter.MyServicesInterface myServicesInterface;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {

//            if (userArrayList.size() >0){
//                Log.d(TAG, "onMapReady: size is more than zero");
//                loadMarkers(googleMap);
//            }
            MapsFragmentArgs args = MapsFragmentArgs.fromBundle(getArguments());
            LatLng sydney;
            if (!args.getFromServices()) {
                sydney = new LatLng(Double.parseDouble(args.getLat()), Double.parseDouble(args.getLng()));

                customerId = args.getUserId();
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Emergency here!"));
                float zoom = 16.0f;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));

            } else {

                getUserData(googleMap);
                getMechServices();

            }


            googleMap.setOnMarkerClickListener(marker -> {


                if (args.getFromServices()) {
                    User user = (User) marker.getTag();
                    Log.d(TAG, "onMapReady: " + user.name);
                    mechId = user.id;
                    Log.d(TAG, "onMapReady: " + args.getFromServices());
                    Log.d(TAG, "onMapReady: size " + mechServicesArrayList.size());

                    for (int i = 0; i < mechServicesArrayList.size(); i++) {
                        Log.d(TAG, "onMapReady: in loop " + mechServicesArrayList.get(i).id);
                        if (mechServicesArrayList.get(i).id.equals(mechId)) {

                            //TODO in adapter, send this arraylist.
                            priceArrayList.add(mechServicesArrayList.get(i));

                            Toast.makeText(getContext(), "marker clicked " + mechId, Toast.LENGTH_SHORT).show();


                        }
                    }

                    //ye this ki jagah kiya lagaun
                    mechanicPriceAdapter = new MechanicPriceAdapter(context, priceArrayList, myServicesInterface);
                    dialogueMechanicServiceBinding.recyler.setAdapter(mechanicPriceAdapter);
                    openPriceDialogue(user);


                } else {

                    DeleteRoomDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.delete_room_dialog,
                            null, false);
                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setView(binding1.getRoot())
                            .create();
                    binding1.text.setText("Do you want to accept the customer?");

                    if (dialog.getWindow() != null)
                        dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    binding1.cancelButton.setText("Cancel");
                    binding1.cancelButton.setOnClickListener(view -> {

                        dialog.dismiss();
                    });

                    binding1.confirmButton.setText("Accept");

                    binding1.confirmButton.setOnClickListener(v -> {


                    });

                    dialog.show();

                }


                return false;
            });


        }
    };

    private void openPriceDialogue(User user) {


            dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);

            Button dialogButton = dialog.findViewById(R.id.call_btn);

            dialogueMechanicServiceBinding.callBtn.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+user.phoneNumber+));
                startActivity(intent);

            });

            dialog.show();


    }

    private void getMechServices() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_SERVICES, null, res -> {

            Log.d(TAG, "getMechServices: " + res);
            for (int i = 0; i < res.length(); i++) {
                try {
                    JSONObject object = res.getJSONObject(i);


                    MechServices mechServices = new MechServices();

                    mechServices.service = object.getString("service_name");
                    mechServices.vehicleType = object.getString("vehicle_type");
                    mechServices.price = object.getInt("price");
                    mechServices.id = object.getString("mechanic");

                    Log.d(TAG, "getMechServices: added");
                    mechServicesArrayList.add(mechServices);

                    if (mechServicesArrayList.size() > 0) {
                        //attach adapter here// with notify on cliock
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "getServices: ", e);
                }

            }


        }, error -> {

            Log.e(TAG, "getServices: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mechServicesArrayList = new ArrayList<>();
        priceArrayList = new ArrayList<>();

        myServicesInterface = this;

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        context = getContext();
        userArrayList = new ArrayList<>();
        actionViews();
//        getUserData();

    }

    private void actionViews() {

        dialogueMechanicServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout. dialogue_mechanic_service, null, false);
        dialog.setContentView(dialogueMechanicServiceBinding.getRoot());



    }

    private void getUserData(GoogleMap googleMap) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebServices.API_GET_USERS, null,
                response -> {


                    //TODO yahan se we'll get users, filter out users with type mech (a number) and then add to arraylist


                    Log.d(TAG, "getUserData: response is  " + response.toString());
                    Log.d(TAG, "actionViews: " + response.length());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject userObj = response.getJSONObject(i);
                            Log.d(TAG, "actionViews: " + userObj.optInt("type"));

                            if (userObj.optInt("type") == 3) {
                                User user = new User();
                                user.email = userObj.optString("email");
                                user.phoneNumber = String.valueOf(userObj.optInt("phone"));
                                user.id = userObj.optString("_id");
                                user.lat = userObj.optString("latitude");
                                user.lng = userObj.optString("longitude");
                                user.name = userObj.optString("name");

                                Log.d(TAG, "getUserData: user name is " + user.name);
                                userArrayList.add(user);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (userArrayList.size() > 0) {
                        loadMarkers(googleMap);
                    }


                }, error -> {

            Log.e(TAG, "removeMech: ", error);

        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    private void loadMarkers(GoogleMap googleMap) {


        for (int i = 0; i < userArrayList.size(); i++) {

            User user = userArrayList.get(i);
            LatLng latlng = new LatLng(Double.parseDouble(user.lat), Double.parseDouble(user.lng));

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(user.name));

            marker.setTag(user);

//            Marker marker = new MarkerOptions().position(latlng).title(user.id);
//            marker.setTag(user);
//            googleMap.addMarker(marker);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

            googleMap.getMaxZoomLevel();

        }
    }

    @Override
    public void onClickService(MechServices mechServices) {
        Log.d(TAG, "onClickService: "+mechServices.service);
    }
}