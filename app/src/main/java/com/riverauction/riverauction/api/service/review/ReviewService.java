package com.riverauction.riverauction.api.service.review;

import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.service.APISuccessResponse;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ReviewService {

    @GET("/api/teachers/{teacherId}/reviews")
    Observable<APISuccessResponse<List<CReview>>> getReviews(@Path("teacherId") Integer teacherId, @Query("next_token") Integer nextToken);
}
