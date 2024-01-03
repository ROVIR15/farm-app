package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.auth.dto.change_password.ChangePasswordRequest
import com.vt.vt.core.data.source.remote.auth.dto.change_password.ChangePasswordResponse
import com.vt.vt.core.data.source.remote.auth.dto.login.LoginRequest
import com.vt.vt.core.data.source.remote.auth.dto.login.LoginResponse
import com.vt.vt.core.data.source.remote.auth.dto.register.request.RegisterRequest
import com.vt.vt.core.data.source.remote.auth.dto.register.response.RegisterResponse
import com.vt.vt.core.data.source.remote.profile.dto.ProfileResponse
import retrofit2.Response
import javax.inject.Inject

class DataRepository @Inject constructor(private val networkService: ApiService) {
    suspend fun registration(registerRequest: RegisterRequest): Response<RegisterResponse> =
        networkService.doRegistration(registerRequest)

    suspend fun getProfile(): Response<ProfileResponse> = networkService.getProfileUser()
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> =
        networkService.doLogin(loginRequest)

    suspend fun logout(): Response<LoginResponse> = networkService.doLogout()

    suspend fun doChangePassword(changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse> =
        networkService.doChangePassword(changePasswordRequest)

}