package com.example.testcounter.data.network

import com.example.testcounter.data.models.Counter
import io.reactivex.Single
import retrofit2.http.*

interface NetworkService {

    @GET("api/v1/counters")
    fun fetchCounters(): Single<List<Counter>>

    @POST("api/v1/counter")
    fun postCounter(@Body counter: Counter): Single<List<Counter>>

    @POST("api/v1/counter/inc")
    fun increaseCounter(@Body counter: Counter): Single<List<Counter>>

    @POST("api/v1/counter/dec")
    fun decreaseCounter(@Body counter: Counter): Single<List<Counter>>

    @HTTP(method = "DELETE", path = "api/v1/counter", hasBody = true)
    fun deleteCounter(@Body counter: Counter): Single<List<Counter>>

}