package com.riverauction.riverauction.feature.consult.write;

import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CImage;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.MvpView;

import java.util.List;

public interface BoardWriteMvpView extends MvpView {
    void successRegist(Boolean boardRegist);
    boolean failRegist(CErrorCause errorCause);
    void successGetUser(CUser user);
    boolean failGetUser(CErrorCause errorCause);
    void successGetReview(CReview response);
    boolean failGetReview(CErrorCause errorCause);
    void successPostProfilePhoto(List<CImage> user);
    boolean failPostProfilePhoto(CErrorCause errorCause);
    boolean failPostPreferences(CErrorCause errorCause);

    void successModify(Boolean boardRegist);
    boolean failModify(CErrorCause errorCause);
}
