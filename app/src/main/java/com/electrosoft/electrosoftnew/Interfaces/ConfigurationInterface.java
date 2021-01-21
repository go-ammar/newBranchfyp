package com.electrosoft.electrosoftnew.Interfaces;

import com.electrosoft.electrosoftnew.models.Devices;
import com.electrosoft.electrosoftnew.models.GetRoom;

import java.util.ArrayList;
import java.util.List;

public interface ConfigurationInterface {

    boolean itemChange(int position, String room_id, ArrayList<GetRoom> getRoomList, String room_name);

    void getDevices();
}
