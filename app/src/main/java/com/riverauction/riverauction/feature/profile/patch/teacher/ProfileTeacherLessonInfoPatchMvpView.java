package com.riverauction.riverauction.feature.profile.patch.teacher;

import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.MvpView;

public interface ProfileTeacherLessonInfoPatchMvpView extends MvpView {
    void successPatchUser(CUser user);
    boolean failPatchUser(CErrorCause errorCause);
}
