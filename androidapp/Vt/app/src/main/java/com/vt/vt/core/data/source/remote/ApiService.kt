package com.vt.vt.core.data.source.remote

import com.vt.vt.core.data.source.remote.auth.model.login.LoginRequest
import com.vt.vt.core.data.source.remote.auth.model.login.LoginResponse
import com.vt.vt.core.data.source.remote.auth.model.register.request.RegisterRequest
import com.vt.vt.core.data.source.remote.auth.model.register.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun doRegistration(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>
}