package com.riverauction.riverauction.feature.consult;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.model.CErrorCause;
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

    //여기부터 시작
    void successGetBoard(CBoard board);
    boolean failGetBoard(CErrorCause errorCause);

    void loadingGetReplyList(Integer boardId);
    void successGetReplyList(Integer boardId, APISuccessResponse<List<CBoard>> response);
    boolean failGetReplyList(Integer boardId, CErrorCause errorCause);

    void successModifyReply(CBoard boardRegist);
    boolean failModifyReply(CErrorCause errorCause);

    void successDeleteReply();
    boolean failDeleteReply(CErrorCause errorCause);

    void successRegistReply(CBoard boardRegist);
    boolean failRegistReply(CErrorCause errorCause);

}
