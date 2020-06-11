package com.electrosoft.electrosoftnew.Models;

public class SensorModel {

    private int Sensor_id;
    private String Sensor_name;
    private double Sensor_maxvalue;

    public SensorModel(int sensor_id, String sensor_name, double sensor_maxvalue) {
        Sensor_id = sensor_id;
        Sensor_name = sensor_name;
        Sensor_maxvalue = sensor_maxvalue;
    }

    public int getSensor_id() {
        return Sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        Sensor_id = sensor_id;
    }

    public String getSensor_name() {
        return Sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        Sensor_name = sensor_name;
    }

    public double getSensor_maxvalue() {
        return Sensor_maxvalue;
    }

    public void setSensor_maxvalue(double sensor_maxvalue) {
        Sensor_maxvalue = sensor_maxvalue;
    }
}
