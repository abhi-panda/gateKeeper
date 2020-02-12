package com.android.silverpanda.gatekeeper;

import org.json.JSONObject;

/**
 * POJO class for Guest
 */
public class Guest extends JSONObject {
    private String uid;
    private String name;
    private int totalAdults;
    private int totalKids;
    private int adultsArrived;
    private int kidsArrived;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", totalAdults=" + totalAdults +
                ", totalKids=" + totalKids +
                ", adultsArrived=" + adultsArrived +
                ", kidsArrived=" + kidsArrived +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalAdults() {
        return totalAdults;
    }

    public void setTotalAdults(int totalAdults) {
        this.totalAdults = totalAdults;
    }

    public int getTotalKids() {
        return totalKids;
    }

    public void setTotalKids(int totalKids) {
        this.totalKids = totalKids;
    }

    public int getAdultsArrived() {
        return adultsArrived;
    }

    public void setAdultsArrived(int adultsArrived) {
        this.adultsArrived = adultsArrived;
    }

    public int getKidsArrived() {
        return kidsArrived;
    }

    public void setKidsArrived(int kidsArrived) {
        this.kidsArrived = kidsArrived;
    }

}

