package com.example.part04_ch01_youtube.service

import com.example.part04_ch01_youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

// retrofit용 서비스
interface VideoService {

    @GET("/v3/8aeab3b9-650b-4174-b4d2-5eca558cfcab")
    fun listVideos(): Call<VideoDto>

}