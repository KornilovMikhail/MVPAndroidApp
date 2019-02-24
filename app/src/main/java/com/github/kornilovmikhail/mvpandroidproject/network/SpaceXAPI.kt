package com.github.kornilovmikhail.mvpandroidproject.network

import com.github.kornilovmikhail.mvpandroidproject.models.ListEvents
import io.reactivex.Single
import retrofit2.http.GET

interface SpaceXAPI {
    @GET("history")
    fun loadCities(): Single<ListEvents>
}