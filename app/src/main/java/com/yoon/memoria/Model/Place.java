package com.yoon.memoria.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoon on 2017-12-27.
 */

public class Place {

    String placeName;
    String placeID;
    String detail;
    String address;

    public Place(){}

    public Place(String placeName, String placeID, String detail, String address){
        this.placeName = placeName;
        this.placeID = placeID;
        this.detail = detail;
        this.address = address;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("placeName", placeName);
        result.put("placeID",placeID);
        result.put("detail", detail);
        result.put("address",address);

        return result;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
