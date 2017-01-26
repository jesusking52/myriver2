package com.riverauction.riverauction.api.service.board;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.BoardWriteRequest;
import com.riverauction.riverauction.api.service.board.params.GetBoardsParams;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;

public interface BoardService {

    @GET("/api/board/{categoryId}")
    Observable<APISuccessResponse<List<CBoard>>> getBoards(@Path("categoryId") Integer categoryId, @QueryMap GetBoardsParams request);

    @GET("/api/board/{boardId}")
    Observable<APISuccessResponse<CBoard>> getBoardDetail(@Path("boardId") Integer boardId);

    @GET("/api/boardreply/{boardId}")
    Observable<APISuccessResponse<List<CBoard>>>  getBoardReply(@Path("boardId") Integer boardId, @Query("userId") Integer userId);

    @GET("/api/boardreply/regist/{boardId}")
    Observable<APISuccessResponse<CBoard>>  postBoardRegist(@Path("boardId") Integer boardId,  @Body BoardWriteRequest request);

    @GET("/api/boardreply/modify/{boardId}")
    Observable<APISuccessResponse<CBoard>>  postBoardModify(@Path("boardId") Integer boardId,  @Body BoardWriteRequest request);

    @DELETE("/api/boarddelete/{boardId}/{replyId}")
    Observable<APISuccessResponse<Void>> deleteBoard(@Path("boardId") Integer boardId, @Path("replyId") Integer replyId);

}
