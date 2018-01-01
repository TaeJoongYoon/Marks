package com.yoon.memoria.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoon on 2017-12-27.
 */

public class Place {

    private String date;
    private String placeName;
    private String placeID;
    private String detail;

    public Place(){}

    public Place(String date, String placeName, String placeID, String detail){
        this.date = date;
        this.placeName = placeName;
        this.placeID = placeID;
        this.detail = detail;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("placeName", placeName);
        result.put("detail", detail);

        return result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
