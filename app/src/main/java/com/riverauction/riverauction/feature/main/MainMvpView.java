package com.riverauction.riverauction.feature.main;

import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CMyTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {
    void successSignOut(Boolean result);
    boolean failSignOut(CErrorCause errorCause);
    void successGetUser(CUser user);
    boolean failGetUser(CErrorCause errorCause);

    void successGetMyTeacher(APISuccessResponse<List<CMyTeacher>> response);

    boolean failGetMyTeacher(CErrorCause errorCause);

    void successConfirmMyTeacher(Boolean response);

    boolean failConfirmMyTeacher(CErrorCause errorCause);
}
