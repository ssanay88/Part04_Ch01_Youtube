package com.example.part04_ch01_youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.part04_ch01_youtube.adapter.VideoAdapter
import com.example.part04_ch01_youtube.dto.VideoDto
import com.example.part04_ch01_youtube.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // fragmentContainer에 프래그먼트를 교체한다.
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayFragment())
            .commit()

        videoAdapter = VideoAdapter()

        // mainRecyclerView에 어댑터와 레이아웃 매니져 연결
        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // api를 통해 비디오 목록을 가져오는 메서드
        getVideoList()

    }


    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideos()
                .enqueue(object : Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.d("MainActivity","response Fail")
                            return
                        }

                        response.body()?.let {
                            Log.d("MainActivity", it.toString())
                            videoAdapter.submitList(it.videos)
                        }




                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        // 예외처리
                    }

                })

        }



    }



}


/*

MotionLayout 사용하기
Exoplayer 사용하기

MotionLayout
- ConstraintLayout 라이브러리의 일부 (서브 클래스)
- 레이아웃 전환과 UI 이동, 크기 조절 및 애니메이션에 사용

ExoPlayer
- Google이 Android SDK 와 별도로 배포되는 오픈소스 프로젝트
- 오디오 및 동영상 재생 가능
- 오디오 및 동영상 재생 관련 강력한 기능들 포함
- 유투브 앱에서 사용하는 라이브러리

Youtube
- Retrofit을 이용하여 영상 목록을 받아와 구성함
- MotionLayout을 이용하여 유투브 영상 플레이어 화면전환 애니메이션을 구현함
- 영상 목록을 클릭하여 ExoPlayer를 이용하여 영상을 재생할 수 있음

 */