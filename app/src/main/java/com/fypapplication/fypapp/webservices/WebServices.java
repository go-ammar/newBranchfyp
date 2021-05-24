package com.fypapplication.fypapp.webservices;

public class WebServices {

    public static final String BASE_URL = "https://fyp-mech.herokuapp.com/";

    public static final String API_SIGNUP = BASE_URL + "api/users/register";
    public static final String API_LOGIN = BASE_URL +"api/users/login";
    public static final String API_GET_USERS = BASE_URL +"api/users";
    public static final String API_REMOVE_MECH = BASE_URL +"api/users/";

    public static final String API_GET_SERVICES = BASE_URL + "api/services";
    public static final String API_DELETE_SERVICES = BASE_URL + "api/services/";
    public static final String API_IS_LOGGED_IN =  BASE_URL + "api/notifications";
    public static final String API_GET_NOTIFCATION_PAYLOAD =  BASE_URL + "api/notifications/getUsers";
    public static final String API_GET_BOOKING = BASE_URL + "api/bookings" ;

    //jsonarrayrequest
    public static final String API_GET_MECHS = BASE_URL + "api/users/mechs/3";
    //add mechanic id after this
    public static final String API_GET_SERVICE_BYMECH = BASE_URL + "api/services/services_by_id/";
    //add mech id after this
    public static final String API_GET_BOOKINGS_BYMECH = BASE_URL + "api/bookings/mech_id/";
    //add customer id after this
    public static final String API_GET_BOOKINGS_BYCUSTOMER = BASE_URL + "api/bookings/customer_id/" ;

    //put api, add user id after this to update. params mae add only device_token in jsonobject
    public static final String API_UPDATE_DEVICE_TOKEN_BY_USER = BASE_URL + "api/notifications/";

    //add userid after this.
    public static final String API_GET_DEVICE_TOKEN_BY_USER = BASE_URL + "api/notifications//notification_user/";

    //delete deviceToken
    public static final String API_DELETE_DEVICE_TOKEN_BY_USER = BASE_URL + "api/notifications/";



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
