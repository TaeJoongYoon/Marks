package com.yoon.memoria.Model;

/**
 * Created by Yoon on 2018-01-29.
 */

public class DBData {
    String lastest_version_code;
    String lastest_version_name;
    String minimum_version_code;
    String minimum_version_name;
    String force_update_message;
    String optional_update_message;

    public DBData(){}

    public DBData(String lastest_version_code,
                  String lastest_version_name,
                  String minimum_version_code,
                  String minimum_version_name,
                  String force_update_message,
                  String optional_update_message){
        this.lastest_version_code = lastest_version_code;
        this.lastest_version_name = lastest_version_name;
        this.minimum_version_code = minimum_version_code;
        this.minimum_version_name = minimum_version_name;
        this.force_update_message = force_update_message;
        this.optional_update_message = optional_update_message;
    }

    public String getLastest_version_code() {
        return lastest_version_code;
    }
    public void setLastest_version_code(String lastest_version_code) {
        this.lastest_version_code = lastest_version_code;
    }
    public String getLastest_version_name() {
        return lastest_version_name;
    }
    public void setLastest_version_name(String lastest_version_name) {
        this.lastest_version_name = lastest_version_name;
    }
    public String getMinimum_version_code() {
        return minimum_version_code;
    }
    public void setMinimum_version_code(String minimum_version_code) {
        this.minimum_version_code = minimum_version_code;
    }
    public String getMinimum_version_name() {
        return minimum_version_name;
    }
    public void setMinimum_version_name(String minimum_version_name) {
        this.minimum_version_name = minimum_version_name;
    }
    public String getForce_update_message() {
        return force_update_message;
    }
    public void setForce_update_message(String forece_update_message) {
        this.force_update_message = forece_update_message;
    }
    public String getOptional_update_message() {
        return optional_update_message;
    }
    public void setOptional_update_message(String optional_update_message) {
        this.optional_update_message = optional_update_message;
    }
}