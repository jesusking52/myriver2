package com.riverauction.riverauction.api.service.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class TeacherReviewRequest {

    public TeacherReviewRequest() {
    }

    public TeacherReviewRequest(String reviewidx, String rank, String review, String teacher_id) {
        this.reviewidx = reviewidx;
        this.rank = rank;
        this.review = review;
        this.teacher_id = teacher_id;
    }


    @JsonProperty("reviewIdx")
    private String reviewidx;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("review")
    private String review;

    @JsonProperty("teacher_id")
    private String teacher_id;

    public static class Builder {
        private String rank;
        private String review;
        private String teacher_id;
        private String reviewidx;

        public Builder setReviewidx(String reviewidx) {
            this.reviewidx = reviewidx;
            return this;
        }

        public Builder setRank(String rank) {
            this.rank = rank;
            return this;
        }

        public Builder setReview(String review) {
            this.review = review;
            return this;
        }
        public Builder setTeacherid(String teacher_id) {
            this.teacher_id = teacher_id;
            return this;
        }

        public TeacherReviewRequest build() {
            return new TeacherReviewRequest(reviewidx, rank, review, teacher_id);
        }
    }
}
