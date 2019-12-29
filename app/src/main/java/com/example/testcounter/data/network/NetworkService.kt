package com.example.testcounter.data.network

import com.example.testcounter.data.models.Counter
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {

    @GET("api/v1/counters")
    fun fetchCounters(): Single<List<Counter>>

    @POST("api/v1/counter")
    fun postCounter(@Body counter: Counter): Single<List<Counter>>

    @POST("api/v1/counter/inc")
    fun increaseCounter(@Body counter: Counter): Single<List<Counter>>

    @POST("api/v1/counter/dec")
    fun decreaseCounter(@Body counter: Counter): Single<List<Counter>>

    @DELETE("api/v1/counter")
    fun deleteCounter(@Body counter: Counter): Single<List<Counter>>

}