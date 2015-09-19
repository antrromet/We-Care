package com.antrromet.wecare.models;

/**
 * Created by Antrromet on 9/7/15 11:12 AM
 */
public class Contact {

    private String id;
    private String website;
    private String email;
    private String fbLink;
    private String twitterLink;

    public String getId() {
        return id;
    }

    public void setId(String campaignId) {
        this.id = campaignId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }
}
