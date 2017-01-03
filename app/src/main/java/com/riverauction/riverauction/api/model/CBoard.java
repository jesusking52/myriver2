package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CBoard {


    @JsonProperty("userid")
    private String userid;

    @JsonProperty("teacherid")
    private String teacherid;

    @JsonProperty("board_idx")
    private Integer boardIdx;

    @JsonProperty("reply_idx")
    private Integer replyIdx;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("view_cnt")
    private Integer viewCnt;

    @JsonProperty("reply_cnt")
    private Integer replyCnt;

    public Integer getBoardIdx() {
        return boardIdx;
    }

    public void setBoardIdx(Integer boardIdx) {
        this.boardIdx = boardIdx;
    }
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(Integer viewCnt) {
        this.viewCnt = viewCnt;
    }

    public Integer getReplyCnt() {
        return replyCnt;
    }

    public void setReplyCnt(Integer replyCnt) {
        this.replyCnt = replyCnt;
    }

    public Integer getReplyIdx() {
        return replyIdx;
    }

    public void setReplyIdx(Integer replyIdx) {
        this.replyIdx = replyIdx;
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
}
