package com.example.gh_train_app.models;

public class Ticket {
    String imageUrl,userID,ticketID,date,person_booked,passenger1,passenger2,passenger3,source,destination,price,arrivaltime,departuretime,booking_status,qrcode;

    public Ticket(){}

    public Ticket(String imageUrl,String userID, String ticketID, String date, String person_booked, String passenger1, String passenger2, String passenger3, String source, String destination, String price, String arrivaltime, String departuretime, String booking_status, String qrcode) {
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.ticketID = ticketID;
        this.date = date;
        this.person_booked = person_booked;
        this.passenger1 = passenger1;
        this.passenger2 = passenger2;
        this.passenger3 = passenger3;
        this.source = source;
        this.destination = destination;
        this.price = price;
        this.arrivaltime = arrivaltime;
        this.departuretime = departuretime;
        this.booking_status = booking_status;
        this.qrcode = qrcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerson_booked() {
        return person_booked;
    }

    public void setPerson_booked(String person_booked) {
        this.person_booked = person_booked;
    }

    public String getPassenger1() {
        return passenger1;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public String getPassenger3() {
        return passenger3;
    }

    public void setPassenger3(String passenger3) {
        this.passenger3 = passenger3;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(String arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
