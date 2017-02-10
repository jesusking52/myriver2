package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CReview {

    @JsonProperty("reviewIdx")
    private Integer reviewIdx;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("review")
    private String review;

    @JsonProperty("userid")
    private String userid;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("teacherid")
    private String teacherid;

    @JsonProperty("createdAt")
    private long createdAt;

    public Integer getReviewIdx() {
        return reviewIdx;
    }

    public void setReviewIdx(Integer reviewIdx) {
        this.reviewIdx = reviewIdx;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
