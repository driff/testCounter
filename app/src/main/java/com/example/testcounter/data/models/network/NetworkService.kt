package com.example.testcounter.data.models.network

import com.example.testcounter.data.models.Counter
import io.reactivex.Single
import retrofit2.http.GET

interface NetworkService {

    @GET("api/v1/counters")
    fun fetchCounters(): Single<List<Counter>>

}