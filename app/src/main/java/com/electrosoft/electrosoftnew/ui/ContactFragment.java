package com.electrosoft.electrosoftnew.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.FragmentContactBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import static androidx.core.content.PermissionChecker.checkSelfPermission;


public class ContactFragment extends Fragment {

    private GoogleMap mMap;
    NavController navController;
    FragmentContactBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        actionViews();
    }

    private void actionViews() {
        binding.fab1.setOnClickListener(view -> {

            makeCall();

        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                makeCall();
            }else {
                Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makeCall(){

        String no = "+923085700715";


        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) requireContext(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);



        }
        else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + no));
            startActivity(callIntent);
        }

    }

    //@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ICCBS = new LatLng(24.9418, 67.1207);
        mMap.addMarker(new MarkerOptions().position(ICCBS).title("karachi univercity"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ICCBS));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ICCBS, 15));
        //mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        //  @Override
        //public void onMapClick(LatLng latLng) {
        //  MarkerOptions markerOptions = new MarkerOptions();
        //markerOptions.position(latLng);
        //markerOptions.title(latLng.latitude+ ":" + latLng.longitude);
        //mMap.clear();
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        //mMap.addMarker(markerOptions);
        //}
        //});

    }
}