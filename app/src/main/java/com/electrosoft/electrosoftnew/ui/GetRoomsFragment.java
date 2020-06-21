package com.electrosoft.electrosoftnew.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.adapters.RoomAdapter;
import com.electrosoft.electrosoftnew.databinding.FragmentGetRoomsBinding;
import com.electrosoft.electrosoftnew.models.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetRoomsFragment extends Fragment {


    private static final String TAG = "GetRoomsFragment";
    private String URL;
    private List<Room> lst;
    NavController navController;
    private RecyclerView recycle;
    private RoomAdapter padapter;
    FragmentGetRoomsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_rooms, container, false );
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        recycle = binding.programmingList;
        recycle.setHasFixedSize( true );
        recycle.setLayoutManager(new LinearLayoutManager(requireContext()) );

        lst = new ArrayList<>();
        actionViews();
    }


    private void actionViews(){}


    private void getromm (){
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Toast.makeText(requireContext(), " access", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++)
                    {

                        jsonObject = response.getJSONObject(i) ;
                        Room r = new Room();
                        r.name = jsonObject.getString("room_name");
                        r.id = jsonObject.getInt("ID");
                        lst.add(r);
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }


                padapter = new RoomAdapter(requireContext(),lst);
                recycle.setAdapter( padapter );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(requireContext(), error+"not access", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);

    }

}
