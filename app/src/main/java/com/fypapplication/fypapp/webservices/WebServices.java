package com.fypapplication.fypapp.webservices;

public class WebServices {

    public static String BASE_URL = "https://fyp-mech.herokuapp.com/";

    public static String API_SIGNUP = BASE_URL + "api/users/register";
    public static String API_LOGIN = BASE_URL +"api/users/login";
    public static String API_GET_USERS = BASE_URL +"api/users";
    public static String API_REMOVE_MECH = BASE_URL +"api/users/";

    public static String API_GET_SERVICES = BASE_URL + "api/services";
    public static String API_DELETE_SERVICES = BASE_URL + "api/services/";
    public static final String API_IS_LOGGED_IN =  BASE_URL + "api/notifications";
    public static final String API_GET_NOTIFCATION_PAYLOAD =  BASE_URL + "api/notifications/getUsers";
    public static final String API_GET_BOOKING = BASE_URL + "api/bookings" ;


    public static String API_PIN = BASE_URL +"api/users/verify_reset_password";
    public static String API_CHANGE_PASSWORD = BASE_URL +"api/users/change_password";
    public static String API_SEND_EMAIL = BASE_URL +"api/users/forget_password/";
    public static String API_GET_ROOMS = BASE_URL +"api/rooms/getByUser";
    public static String API_GET_DEVICES = BASE_URL + "api/devices/getByUser";
    public static String API_ADD_ROOM = BASE_URL + "api/rooms";
    public static String API_UPDATE_ROOM = BASE_URL + "api/rooms/";
    public static String API_DELETE_ROOM = BASE_URL + "api/devices/";

    //TODO add ID after URL and then add /assignRoom at end
    public static String API__UPDATE_CONFIG_DEVICE = BASE_URL + "api/devices/";



    //TODO add ID after this URL
    public static String API_GET_SENSORS = BASE_URL + "api/sensors/allDeviceSensors/";



}
