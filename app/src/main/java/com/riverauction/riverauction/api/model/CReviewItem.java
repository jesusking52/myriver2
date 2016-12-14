package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CReviewItem {

    @JsonProperty("reviewIdx")
    private Integer reviewIdx;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("createAt")
    private String createAt;

    @JsonProperty("review")
    private String review;

    public Integer getReviewIdx() {
        return reviewIdx;
    }

    public void setReviewIdx(Integer reviewIdx) {
        this.reviewIdx = reviewIdx;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
