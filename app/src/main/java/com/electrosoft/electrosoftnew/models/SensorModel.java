package com.electrosoft.electrosoftnew.models;

public class SensorModel {

    public int Sensor_id;
    public String Sensor_name;
    public double Sensor_maxvalue;

    public SensorModel(int sensor_id, String sensor_name, double sensor_maxvalue) {
        Sensor_id = sensor_id;
        Sensor_name = sensor_name;
        Sensor_maxvalue = sensor_maxvalue;
    }


    public SensorModel() {

    }
}
