package com.example.gh_train_app.models;

public class Book {
    public String booking_id;
    public String booking_code;
    public String booking_status;
    public String train_id;
    public String train_number;
    public String train_name;
    public String train_type;
    public String train_fare;
    public String train_source;
    public String train_destination;
    public String train_arrival_time;
    public String train_depature_time;
    public String person_booked;
    public String passenger_one_booked;
    public String passenger_two_booked;
    public String passenger_three_booked;
    public String date_booked;
    public String imageUrl;

    public Book(){}

    public Book(String booking_id,String booking_code, String booking_status, String train_id,String train_number, String train_name, String train_type, String train_fare, String train_source, String train_destination, String train_arrival_time, String train_depature_time, String person_booked, String passenger_one_booked, String passenger_two_booked, String passenger_three_booked, String date_booked, String imageUrl) {
        this.booking_id = booking_id;
        this.booking_code = booking_code;
        this.booking_status = booking_status;
        this.train_id = train_id;
        this.train_number = train_number;
        this.train_name = train_name;
        this.train_type = train_type;
        this.train_fare = train_fare;
        this.train_source = train_source;
        this.train_destination = train_destination;
        this.train_arrival_time = train_arrival_time;
        this.train_depature_time = train_depature_time;
        this.person_booked = person_booked;
        this.passenger_one_booked = passenger_one_booked;
        this.passenger_two_booked = passenger_two_booked;
        this.passenger_three_booked = passenger_three_booked;
        this.date_booked = date_booked;
        this.imageUrl = imageUrl;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_code() {
        return booking_code;
    }

    public void setBooking_code(String booking_code) {
        this.booking_code = booking_code;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_type() {
        return train_type;
    }

    public void setTrain_type(String train_type) {
        this.train_type = train_type;
    }

    public String getTrain_fare() {
        return train_fare;
    }

    public void setTrain_fare(String train_fare) {
        this.train_fare = train_fare;
    }

    public String getTrain_source() {
        return train_source;
    }

    public void setTrain_source(String train_source) {
        this.train_source = train_source;
    }

    public String getTrain_destination() {
        return train_destination;
    }

    public void setTrain_destination(String train_destination) {
        this.train_destination = train_destination;
    }

    public String getTrain_arrival_time() {
        return train_arrival_time;
    }

    public void setTrain_arrival_time(String train_arrival_time) {
        this.train_arrival_time = train_arrival_time;
    }

    public String getTrain_depature_time() {
        return train_depature_time;
    }

    public void setTrain_depature_time(String train_depature_time) {
        this.train_depature_time = train_depature_time;
    }

    public String getPerson_booked() {
        return person_booked;
    }

    public void setPerson_booked(String person_booked) {
        this.person_booked = person_booked;
    }

    public String getPassenger_one_booked() {
        return passenger_one_booked;
    }

    public void setPassenger_one_booked(String passenger_one_booked) {
        this.passenger_one_booked = passenger_one_booked;
    }

    public String getPassenger_two_booked() {
        return passenger_two_booked;
    }

    public void setPassenger_two_booked(String passenger_two_booked) {
        this.passenger_two_booked = passenger_two_booked;
    }

    public String getPassenger_three_booked() {
        return passenger_three_booked;
    }

    public void setPassenger_three_booked(String passenger_three_booked) {
        this.passenger_three_booked = passenger_three_booked;
    }

    public String getDate_booked() {
        return date_booked;
    }

    public void setDate_booked(String date_booked) {
        this.date_booked = date_booked;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

