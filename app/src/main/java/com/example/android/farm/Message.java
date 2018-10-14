package com.example.android.farm;

/**
 * Created by Tasos on 24-Dec-17.
 */

public class Message {

    private String Problem_id;
    private String Message_Id;
    private  String Geoponos_id;
    private String Georgos_id;
    private  String Message;


    public Message(String problem_id, String message_Id, String geoponos_id, String georgos_id, String message) {
        Problem_id = problem_id;
        Message_Id = message_Id;
        Geoponos_id = geoponos_id;
        Georgos_id = georgos_id;
        Message = message;
    }

    public String getProblem_id() {
        return Problem_id;
    }

    public void setProblem_id(String problem_id) {
        Problem_id = problem_id;
    }

    public String getMessage_Id() {
        return Message_Id;
    }

    public void setMessage_Id(String message_Id) {
        Message_Id = message_Id;
    }

    public String getGeoponos_id() {
        return Geoponos_id;
    }

    public void setGeoponos_id(String geoponos_id) {
        Geoponos_id = geoponos_id;
    }

    public String getGeorgos_id() {
        return Georgos_id;
    }

    public void setGeorgos_id(String georgos_id) {
        Georgos_id = georgos_id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}