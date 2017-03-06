package com.riverauction.riverauction.api.service.board;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.BoardWriteRequest;
import com.riverauction.riverauction.api.service.board.params.GetBoardsParams;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
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

    @POST("/api/board/{userId}/board_write")
    Observable<APISuccessResponse<Boolean>> postBoardRegist(@Path("userId") Integer boardId,  @Body BoardWriteRequest request);

    @POST("/api/board/{userId}/board_modify")
    Observable<APISuccessResponse<Boolean>>  postBoardModify(@Path("userId") Integer boardId,  @Body BoardWriteRequest request);

    @POST("/api/board/{userId}/board_delete")
    Observable<APISuccessResponse<Boolean>> deleteBoard(@Path("userId") Integer boardId,  @Body BoardWriteRequest request);


}
