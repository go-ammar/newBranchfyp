package com.electrosoft.electrosoftnew.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.electrosoft.electrosoftnew.Interfaces.RoomInterface;
import com.electrosoft.electrosoftnew.R;
import com.electrosoft.electrosoftnew.databinding.ListGetroomBinding;
import com.electrosoft.electrosoftnew.models.GetRoom;
import com.electrosoft.electrosoftnew.models.Room;
import com.electrosoft.electrosoftnew.ui.GetRoomsFragmentDirections;

import java.util.List;

import static java.util.Objects.isNull;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private Context mContext;
    private List<Room> mdata;

    private List<GetRoom> getRoomList;
    //  private SensorPerRoomAdapter spradapter;
    RecyclerView recyclerView;
    private RoomInterface roomInterface;

    //ArrayList<SensorsPerRoom> list = new ArrayList<>();

    public RoomAdapter(Context mContext, List<GetRoom> mdata, RoomInterface roomInterface) {
        this.mContext = mContext;
        this.getRoomList = mdata;
        this.roomInterface = roomInterface;
    }


    @NonNull
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListGetroomBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_getroom, parent, false);

        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GetRoom room = getRoomList.get(0);
        ((RoomViewHolder) holder).setBinding(room);


        Log.d("TAG", "onBindViewHolder: dataset on bind " + position);

        Log.d("TAG", "onBindViewHolder: data size " + getRoomList.get(0).data.size());
//        if (getRoomList.get(position).data.size() > 0)
        ((RoomViewHolder) holder).binding.roomNameTextview.setText(getRoomList.get(0).data.get(position).name);
//        else
//            ((RoomViewHolder) holder).binding.viewr.setVisibility(View.GONE);

        ((RoomViewHolder) holder).binding.updateroom.setOnClickListener(v -> {
//            showUpdateDialog(position);
            roomInterface.updateDialog(position);


        });

//        ((RoomViewHolder) holder).binding.updateroom.setOnClickListener(v -> {
//            roomInterface.updateDialog(position);
//
//        });

        ((RoomViewHolder) holder).binding.deletesensor.setOnClickListener(v -> {
//            showDeleteDialog(position);
            roomInterface.deleteDialog(position);


        });

        ((RoomViewHolder) holder).binding.viewallsensor.setOnClickListener(v -> {
            Log.d("TAG", "onBindViewHolder: Size is " + getRoomList.size());
            ;
            //TODO remove dummy data

            GetRoomsFragmentDirections.ActionNavGetRoomsToSensorPerRoomFragment action =
                    GetRoomsFragmentDirections.actionNavGetRoomsToSensorPerRoomFragment();
            Log.d("TAG", "onBindViewHolder: id is " + (getRoomList.get(0).data.get(position).id));
            action.setId(getRoomList.get(0).data.get(position).id);


            Navigation.createNavigateOnClickListener(action).
                    onClick(((RoomViewHolder) holder).binding.viewallsensor);

//            Navigation.createNavigateOnClickListener(action);

            getRoomList.clear();


//            SensorDialogBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.sensor_dialog,
//                    null, false);
//
//
//
//
//            SensorsPerRoom spr = new SensorsPerRoom();
//            spr.description = "des";
//            spr.name = "name";
//            list.add(spr);
//            SensorsPerRoom spr2 = new SensorsPerRoom();
//            spr2.description = "des2";
//            spr2.name = "name2";
//            list.add(spr2);

            // viewSensors();


        });


    }

    private void showUpdateDialog(int position) {
//        UpdateRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.update_room_dialog,
//                null, false);
//        AlertDialog dialog = new AlertDialog.Builder(mContext)
//                .setView(binding.getRoot())
//                .create();
//
//        if (dialog.getWindow() != null)
//            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        dialog.show();
//
//        binding.CancelBtn.setOnClickListener(view -> {
//            dialog.dismiss();
//        });
//
//        binding.updateRoomBtn.setOnClickListener(view -> {
//            dialog.dismiss();
//
//            JSONObject params = new JSONObject();
//
//            try {
//                // BODY
//                params.put("name", binding.RoomIdET.getText().toString());
//            } catch (Exception e) {
//                Log.e("TAG", "_apiLogin: ", e);
//            }
//
//
//            SharedPrefs sharedPrefs = new SharedPrefs(mContext);
//
//
//            //TODO checking values for updates
//
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
//                    WebServices.API_UPDATE_ROOM + getRoomList.get(0).data.get(position).id,
//                    params, response -> {
//
//
////                notifyItemChanged(position);
//
//                notifyItemChanged(position);
//
//                Log.d("TAG", "_apiUpdateRoom: res " + response);
//                Toast toast = Toast.makeText(mContext, "Room updated", Toast.LENGTH_SHORT);
//                toast.show();
//
//
//
//            }, error -> {
//                Toast toast = Toast.makeText(mContext, "Could not update room", Toast.LENGTH_SHORT);
//                toast.show();
//                Log.d("TAG", "_apiUpdateRoom: error " + error);
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap header = new HashMap<>();
//                    header.put("Authorization", sharedPrefs.getKey());
//                    Log.d("TAG", "getHeaders: " + header.toString());
//                    return header;
//                }
//            };
//
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//
//
//        });

    }

    public void showDeleteDialog(int position) {


//        DeleteRoomDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.delete_room_dialog,
//                null, false);
//
//        AlertDialog dialog = new AlertDialog.Builder(mContext)
//                .setView(binding.getRoot())
//                .create();
//        if (dialog.getWindow() != null)
//            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
//        binding.cancelButton.setOnClickListener(view -> {
//            dialog.dismiss();
//        });
//
//        binding.confirmButton.setOnClickListener(view -> {
////            dialog.dismiss();
//
//
//            JSONObject params = new JSONObject();
//            SharedPrefs sharedPrefs = new SharedPrefs(mContext);
//
//
//            Log.d("TAG", "showDeleteDialog: " + getRoomList.get(0).data.get(position).id);
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
//                    WebServices.API_DELETE_ROOM + getRoomList.get(0).data.get(position).id,
//                    params, response -> {
//
//                Log.d("TAG", "_apigetRoom: res " + response);
//
//
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
//                        .create();
//                Toast toast = Toast.makeText(mContext, "Room deleted successfully", Toast.LENGTH_SHORT);
//                toast.show();
//
//
//            }, error -> {
//                Toast toast = Toast.makeText(mContext, "Room could not be deleted", Toast.LENGTH_SHORT);
//                toast.show();
//                dialog.dismiss();
//
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap header = new HashMap<>();
//                    header.put("Authorization", sharedPrefs.getKey());
//                    Log.d("TAG", "getHeaders: " + header.toString());
//                    return header;
//                }
//            };
//
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//
//
//        });


    }


    public int getItemCount() {
//        Log.d("TAG", "getItemCount: for rooms " + getRoomList.get(0).data.size());
//        Log.d("TAG", "getItemCount: data in room " + getRoomList.get(0).data.get(0).name);
            return getRoomList.get(0).data.size();
    }


    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        ListGetroomBinding binding;


        public RoomViewHolder(ListGetroomBinding binding) {

            super(binding.getRoot());
            this.binding = binding;
        }

        void setBinding(GetRoom room) {
            binding.setRoom(room);
            binding.executePendingBindings();
        }


    }

}
