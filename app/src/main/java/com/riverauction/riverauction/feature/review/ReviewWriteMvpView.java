package com.riverauction.riverauction.feature.review;

import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.MvpView;

public interface ReviewWriteMvpView extends MvpView {
    void successWriteReview(Boolean user);
    boolean failWriteReview(CErrorCause errorCause);
    void successGetUser(CUser user);
    boolean failGetUser(CErrorCause errorCause);
    void successGetReview(CReview response);
    boolean failGetReview(CErrorCause errorCause);

    void successModifyReview(Boolean result);

    boolean failModifyReview(CErrorCause errorCause);
}
