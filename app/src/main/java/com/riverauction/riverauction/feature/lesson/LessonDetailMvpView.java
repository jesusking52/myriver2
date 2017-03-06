package com.riverauction.riverauction.feature.lesson;

import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLesson;
import com.riverauction.riverauction.api.model.CLessonBidding;
import com.riverauction.riverauction.api.model.CLessonFavorite;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.MvpView;

import java.util.List;

public interface LessonDetailMvpView extends MvpView {
    void successGetLesson(CLesson lesson);
    boolean failGetLesson(CErrorCause errorCause);
    void successPostLessonFavorites(CLessonFavorite lessonFavorite);
    boolean failPostLessonFavorites(CErrorCause errorCause);
    void successDeleteLessonFavorites();
    boolean failDeleteLessonFavorites(CErrorCause errorCause);
    void successCancelLesson(CLesson lesson);
    boolean failCancelLesson(CErrorCause errorCause);

    void successGetMyBidding(APISuccessResponse<List<CLessonBidding>> response);

    boolean failGetMyBidding(CErrorCause errorCause);
}
