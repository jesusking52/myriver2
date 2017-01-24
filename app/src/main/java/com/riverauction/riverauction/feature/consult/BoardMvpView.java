package com.riverauction.riverauction.feature.consult;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLesson;
import com.riverauction.riverauction.base.MvpView;

import java.util.List;

public interface BoardMvpView extends MvpView {
    // student
    //void successGetActiveLessonAndBiddings(CLesson lesson, List<CLessonBidding> lessonBiddingList, Integer nextToken, int totalCount);
    //void successGetLessonBiddings(List<CLessonBidding> lessonBiddingList, Integer nextToken);
    //boolean failGetLessonBiddings(CErrorCause errorCause);
    //void loadingGetActiveLessonList();
    // active
    //void successGetActiveList(List<CLesson> lessons, Integer nextToken);
    //boolean failGetActiveList(CErrorCause errorCause);
    //void loadingGetActiveList();
    // history
    void successBoardList(Integer boardid, List<CBoard> lessons, Integer nextToken);
    boolean failGetBoardList(Integer boardid,CErrorCause errorCause);
    void loadingGetBoardList(Integer boardid);

    //void successCancelLesson(CLesson lesson);
    //boolean failCancelLesson(CErrorCause errorCause);
}
