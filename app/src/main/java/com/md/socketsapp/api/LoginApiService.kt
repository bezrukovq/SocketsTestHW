package com.md.socketsapp.api

import com.md.socketsapp.models.LoginResponse
import com.md.socketsapp.models.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService{
    @POST("login")
    fun login(@Body user: User): Single<LoginResponse>
}
