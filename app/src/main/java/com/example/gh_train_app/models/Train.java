package com.example.gh_train_app.models;

public class Train {
    public String train_id;
    public String train_name;
    public String train_number;
    public String station;
    public String type;
    public String source;
    public String dest;
    public String arrival_time;
    public String depart_time;
    public int avail_seats;
    public int real_avail_seats;
    public String description;
    public String working_day;
    public String price;
    public String imageUrl;

    public Train(){}

    public Train(String train_id, String train_name, String train_number, String station, String type, String source, String dest, String arrival_time, String depart_time, int avail_seats,int real_avail_seats, String description, String working_day, String price, String imageUrl) {
        this.train_id = train_id;
        this.train_name = train_name;
        this.train_number = train_number;
        this.station = station;
        this.type = type;
        this.source = source;
        this.dest = dest;
        this.arrival_time = arrival_time;
        this.depart_time = depart_time;
        this.avail_seats = avail_seats;
        this.real_avail_seats = real_avail_seats;
        this.description = description;
        this.working_day = working_day;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }

    public int getAvail_seats() {
        return avail_seats;
    }

    public void setAvail_seats(int avail_seats) {
        this.avail_seats = avail_seats;
    }

    public int getReal_avail_seats() {
        return real_avail_seats;
    }

    public void setReal_avail_seats(int real_avail_seats) {
        this.real_avail_seats = real_avail_seats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorking_day() {
        return working_day;
    }

    public void setWorking_day(String working_day) {
        this.working_day = working_day;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
