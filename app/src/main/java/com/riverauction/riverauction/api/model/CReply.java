package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CReply {


    @JsonProperty("idx")
    private Integer idx;

    @JsonProperty("userid")
    private String userid;

    @JsonProperty("teacherid")
    private String teacherid;

    @JsonProperty("createdAt")
    private long createdAt;

    @JsonProperty("title")
    private String title;

    @JsonProperty("contents")
    private String contents;

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer rank) {
        this.idx = idx;
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

    public void setGetCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
