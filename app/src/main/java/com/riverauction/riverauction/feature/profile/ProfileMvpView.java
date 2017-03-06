package com.riverauction.riverauction.feature.profile;

import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.MvpView;

public interface ProfileMvpView extends MvpView {
    void successSignOut(Boolean result);
    boolean failSignOut(CErrorCause errorCause);

    void successPostPreferences(CUser user);
    boolean failPostPreferences(CErrorCause errorCause);

    void successPostProfilePhoto(CUser user);
    boolean failPostProfilePhoto(CErrorCause errorCause);

    void successDropOut(Boolean boardRegist);

    boolean failDropOut(CErrorCause errorCause);
}
