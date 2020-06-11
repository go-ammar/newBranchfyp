package com.electrosoft.electrosoftnew.Models;

public class RoomModel {

    private int Room_id;
    private String Room_name;
    private String Room_description;

    public RoomModel(int room_id, String room_name, String room_description) {
        Room_id = room_id;
        Room_name = room_name;
        Room_description = room_description;
    }

    public int getRoom_id() {
        return Room_id;
    }

    public String getRoom_name() {
        return Room_name;
    }

    public String getRoom_description() {
        return Room_description;
    }

    public void setRoom_id(int room_id) {
        Room_id = room_id;
    }

    public void setRoom_name(String room_name) {
        Room_name = room_name;
    }

    public void setRoom_description(String room_description) {
        Room_description = room_description;
    }
}
