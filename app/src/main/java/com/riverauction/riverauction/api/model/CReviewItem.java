package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CReviewItem {

    @JsonProperty("reviewIdx")
    private Integer reviewIdx;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("createAt")
    private CharSequence createAt;

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

    public CharSequence getCreateAt() {
        return createAt;
    }

    public void setCreateAt(CharSequence createAt) {
        this.createAt = createAt;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
