package com.antrromet.wecare.models;

/**
 * Created by Antrromet on 9/14/15 12:42 AM
 */
public class Ngo {

    private String id;
    private int campaignCount;
    private String img;
    private String mission;
    private String name;
    private String shortDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public int getCampaignCount() {
        return campaignCount;
    }

    public void setCampaignCount(int campaignCount) {
        this.campaignCount = campaignCount;
    }
}
