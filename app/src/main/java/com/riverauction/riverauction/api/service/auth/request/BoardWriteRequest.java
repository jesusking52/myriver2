package com.riverauction.riverauction.api.service.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class BoardWriteRequest {

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

    @JsonProperty("category2_id")
    private Integer category2Id;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("content")
    private String content;

    @JsonProperty("view_cnt")
    private Integer viewCnt;

    @JsonProperty("reply_cnt")
    private Integer replyCnt;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("name")
    private String name;

    public BoardWriteRequest() {
    }



    public BoardWriteRequest(String userid, String teacherid, Integer boardIdx, Integer replyIdx, Long createdAt, Integer categoryId, Integer category2Id, String content, Integer viewCnt, String subject, Integer replyCnt, String imagePath, String name) {
        this.userid = userid;
        this.teacherid = teacherid;
        this.boardIdx = boardIdx;
        this.replyIdx = replyIdx;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.category2Id = category2Id;
        this.content = content;
        this.viewCnt = viewCnt;
        this.subject = subject;
        this.replyCnt = replyCnt;
        this.imagePath = imagePath;
        this.name = name;
    }

    public static class Builder {
        private String userid;

        private String teacherid;

        private Integer boardIdx;

        private Integer replyIdx;

        private Long createdAt;

        private Integer categoryId;

        private Integer category2Id;

        private String content;

        private Integer viewCnt;

        private Integer replyCnt;

        private String subject;

        private String imagePath;

        private String name;

        public Builder setUserid(String userid) {
            this.userid = userid;
            return this;
        }

        public String getImagePath() {
            return imagePath;
        }

        public Builder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder setTeacherid(String teacherid) {
            this.teacherid = teacherid;
            return this;
        }

        public Builder setBoardIdx(Integer boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public Builder setReplyIdx(Integer replyIdx) {
            this.replyIdx = replyIdx;
            return this;
        }

        public Builder setCreatedAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder setCategory2Id(Integer category2Id) {
            this.category2Id = category2Id;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setViewCnt(Integer viewCnt) {
            this.viewCnt = viewCnt;
            return this;
        }

        public Builder setReplyCnt(Integer replyCnt) {
            this.replyCnt = replyCnt;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public BoardWriteRequest build() {
            return new BoardWriteRequest( userid,  teacherid,  boardIdx,  replyIdx,  createdAt,  categoryId, category2Id,  content,  viewCnt, subject, replyCnt, imagePath, name);
        }
    }
}
