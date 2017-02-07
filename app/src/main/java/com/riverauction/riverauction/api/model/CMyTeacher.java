package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CMyTeacher {

    @JsonProperty("checked_user_id")
    private String checkedUserId;

    @JsonProperty("create_at")
    private long createAt;

    @JsonProperty("user_id")
    private Integer userId;

    public String getCheckedUserId() {
        return checkedUserId;
    }

    public void setCheckedUserId(String checkedUserId) {
        this.checkedUserId = checkedUserId;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
