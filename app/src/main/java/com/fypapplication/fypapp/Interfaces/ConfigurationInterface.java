package com.fypapplication.fypapp.Interfaces;

import com.fypapplication.fypapp.models.GetRoom;

import java.util.ArrayList;

public interface ConfigurationInterface {

    boolean itemChange(int position, String room_id, ArrayList<GetRoom> getRoomList, String room_name);

    void getDevices();
}
