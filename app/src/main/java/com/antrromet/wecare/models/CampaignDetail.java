package com.antrromet.wecare.models;

import java.util.List;

/**
 * Created by Antrromet on 9/7/15 10:52 AM
 */
public class CampaignDetail {

    private String id;
    private String about;
    private List<Activity> activities;
    private Contact contact;
    private String img;
    private String startsOn;
    private String endsOn;
    private String mission;
    private String name;
    private String ngoId;
    private String ngoName;
    private String ngoShortDesc;
    private int progress;
    private String shortDesc;
    private String subTitle;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(String startsOn) {
        this.startsOn = startsOn;
    }

    public String getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(String endsOn) {
        this.endsOn = endsOn;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
