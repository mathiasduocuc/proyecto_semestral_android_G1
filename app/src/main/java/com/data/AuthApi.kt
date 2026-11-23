package com.data

import com.model.LoginRequest
import com.model.LoginResponse
import com.model.RegisterRequest
import com.model.RecoverRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface AuthApi {

    @DELETE("api/auth/delete/{correo}")
    suspend fun deleteUser(@Path("correo") correo: String): Response<String>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<String>

    @PUT("api/auth/recover")
    suspend fun recoverPassword(@Body request: RecoverRequest): Response<String>
}