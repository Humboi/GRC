package com.example.gh_train_app.models;

public class TrainStation {
    public String station_id;
    public String station_name;
    public String station_number;
    public String station_description;
    public String closing_time;
    public String opening_time;

    public TrainStation(){

    }

    public TrainStation(String station_id, String station_name, String station_number, String station_description, String closing_time, String opening_time) {
        this.station_id = station_id;
        this.station_name = station_name;
        this.station_number = station_number;
        this.station_description = station_description;
        this.closing_time = closing_time;
        this.opening_time = opening_time;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_number() {
        return station_number;
    }

    public void setStation_number(String station_number) {
        this.station_number = station_number;
    }

    public String getStation_description() {
        return station_description;
    }

    public void setStation_description(String station_description) {
        this.station_description = station_description;
    }

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }
}
