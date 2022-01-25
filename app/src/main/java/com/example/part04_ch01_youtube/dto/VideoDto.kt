package com.example.part04_ch01_youtube.dto

import com.example.part04_ch01_youtube.model.VideoModel

// 서버에서 데이터를 받아오는 형태 Data Transfer Object
data class VideoDto(
    val videos: List<VideoModel>
)
