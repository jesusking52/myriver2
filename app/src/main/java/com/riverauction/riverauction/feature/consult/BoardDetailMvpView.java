package com.riverauction.riverauction.feature.consult;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLesson;
import com.riverauction.riverauction.api.model.CLessonFavorite;
import com.riverauction.riverauction.api.model.CReply;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.MvpView;

import java.util.List;


/*
1. 질문 데이터 로드
2. 질문 데이터 실패
3. 답글 데이터 로드
4. 답글 데이터 실패
5. 수정(답글 수정 포함)
6. 수정 실패
7. 삭제(답글 삭제 포함)
8. 삭제 실패
9. 답글
10. 답글 실패
 */
public interface BoardDetailMvpView extends MvpView {
/*
    void successGetLesson(CLesson lesson);
    boolean failGetLesson(CErrorCause errorCause);
    void successPostLessonFavorites(CLessonFavorite lessonFavorite);
    boolean failPostLessonFavorites(CErrorCause errorCause);
    void successDeleteLessonFavorites();
    boolean failDeleteLessonFavorites(CErrorCause errorCause);
    void successCancelLesson(CLesson lesson);
    boolean failCancelLesson(CErrorCause errorCause);
*/
    //여기부터 시작
    void successGetBoard(CBoard board);
    boolean failGetBoard(CErrorCause errorCause);

    void successGetReply( APISuccessResponse<List<CBoard>> response);
    boolean failGetReply(CErrorCause errorCause);

    void successModifyReply();
    boolean failModifyReply(CErrorCause errorCause);

    void successDeleteReply();
    boolean failDeleteReply(CErrorCause errorCause);

    void successRegistReply();
    boolean failRegistReply(CErrorCause errorCause);

}
