package com.antrromet.wecare.models;

/**
 * Created by Antrromet on 9/3/15 3:33 PM
 */
public class Campaign {

    private String id;
    private String img;
    private String mission;
    private String name;
    private String ngoId;
    private String ngoName;
    private String ngoShortDesc;
    private String shortDesc;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getNgoShortDesc() {
        return ngoShortDesc;
    }

    public void setNgoShortDesc(String ngoShortDesc) {
        this.ngoShortDesc = ngoShortDesc;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }
}
