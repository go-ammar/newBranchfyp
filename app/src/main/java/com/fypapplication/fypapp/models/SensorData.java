package com.fypapplication.fypapp.models;

import java.sql.Date;
import java.util.List;

public class SensorData {
    public boolean isActive;
    public String name;
    public String unit;
    public String device;
    public Date createdAt;
    public Date updatedAt;
    public String id;
    public List<SensorReading> readings;
}
