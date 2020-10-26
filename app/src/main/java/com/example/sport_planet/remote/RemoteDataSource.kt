package com.example.sport_planet.remote

import com.example.sport_planet.model.ExerciseResponse
import com.example.sport_planet.model.LoginResponse
import com.example.sport_planet.model.RegionResponse
import com.example.sport_planet.model.ServerCallBackResponse
import io.reactivex.Single
import retrofit2.http.Body

interface RemoteDataSource{
    fun getExercise() : Single<ExerciseResponse>

    fun getRegion() : Single<RegionResponse>

    fun postSignIn(userInfo: LoginResponse): Single<ServerCallBackResponse>
}