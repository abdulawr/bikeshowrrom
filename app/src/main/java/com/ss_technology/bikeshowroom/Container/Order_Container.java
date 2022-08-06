package com.ss_technology.bikeshowroom.Container;

import java.io.Serializable;

public class Order_Container implements Serializable {
    String id,bikeID,date,total,sell_type,type,c_payment_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBikeID() {
        return bikeID;
    }

    public void setBikeID(String bikeID) {
        this.bikeID = bikeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSell_type() {
        return sell_type;
    }

    public void setSell_type(String sell_type) {
        this.sell_type = sell_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getC_payment_type() {
        return c_payment_type;
    }

    public void setC_payment_type(String c_payment_type) {
        this.c_payment_type = c_payment_type;
    }
}
