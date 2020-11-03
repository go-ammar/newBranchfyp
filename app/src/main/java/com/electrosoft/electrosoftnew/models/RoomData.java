package com.electrosoft.electrosoftnew.models;

import java.util.Date;
import java.util.List;

public class RoomData {
    public String name = "";
    public String createdBy = "";
    public Date createdAt;
    public Date updatedAt;
    public String id = "";
    public List<RoomDevices> devices;
}
