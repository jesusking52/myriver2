package com.riverauction.riverauction.api.service.board;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.service.APISuccessResponse;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface BoardService {

    @GET("/api/board/{categoryId}")
    Observable<APISuccessResponse<List<CBoard>>> getBoards(@Path("categoryId") Integer userId, @Query("next_token") Integer nextToken);
}
