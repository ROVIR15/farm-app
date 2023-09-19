package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.auth.model.login.LoginRequest
import com.vt.vt.core.data.source.remote.auth.model.login.LoginResponse
import com.vt.vt.core.data.source.remote.auth.model.register.request.RegisterRequest
import com.vt.vt.core.data.source.remote.auth.model.register.response.RegisterResponse
import com.vt.vt.core.data.source.remote.auth.model.user_session.UserSession
import retrofit2.Response
import javax.inject.Inject

class DataRepository @Inject constructor(private val networkService: ApiService) {
    suspend fun registration(registerRequest: RegisterRequest): Response<RegisterResponse> =
        networkService.doRegistration(registerRequest)

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> =
        networkService.doLogin(loginRequest)

    suspend fun logout(): Response<LoginResponse> = networkService.doLogout()

}