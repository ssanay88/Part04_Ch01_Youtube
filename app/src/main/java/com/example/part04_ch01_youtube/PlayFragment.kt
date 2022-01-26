package com.example.part04_ch01_youtube

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.part04_ch01_youtube.adapter.VideoAdapter
import com.example.part04_ch01_youtube.databinding.FragmentPlayerBinding
import com.example.part04_ch01_youtube.dto.VideoDto
import com.example.part04_ch01_youtube.service.VideoService
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayFragment : Fragment(R.layout.fragment_player) {

    private var binding: FragmentPlayerBinding? = null    // 뷰바인딩 설정
    private lateinit var videoAdapter: VideoAdapter
    private var player: SimpleExoPlayer? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)    // 프래그먼트에서 뷰를 바인딩하기
        binding = fragmentPlayerBinding

        videoAdapter = VideoAdapter(callback = { url, title ->
            playWithUrl(url,title)
        })

        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerVeiw(fragmentPlayerBinding)
        initPlayer(fragmentPlayerBinding)
        initControlBtn(fragmentPlayerBinding)

        getVideoList()


    }

    private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding) {

        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            // 움직이고 있을 때
            // Fragment MotionLayout이 움직이고 있을 때 MainActivity MotionLayout에게 얼만큼 이동 중인지 값을 제공
            // 제공된 값을 토대로 바텀 내비게이션 숨김 처리
            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding?.let {
                    // 프래그먼트는 단독으로 존재하지 못하므로 프래그먼트가 붙어있는 액티비티를 가져온다.
                    // 여기서는 MainActivity를 불러와서 MainMotionLayout의 진행도에 FragmentMotionLayout의 진행도를 전해준다.
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })

    }

    // 화면을 올렸을 때 밑에 리사이클러뷰를 띄운다.
    private fun initRecyclerVeiw(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.fragmentRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun initPlayer(fragmentPlayerBinding: FragmentPlayerBinding) {

        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        // PlayerView의 플레이어로 생성한 player
        fragmentPlayerBinding.playerView.player = player


        binding?.let {
            // 플레이어에 추가 리스너 설정
            player?.addListener(object : Player.EventListener{
                // 플레잉 여부가 바뀔때마다
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying) {
                        it.bottomPlayerControlBtn.setImageResource(R.drawable.ic_baseline_pause_24)

                    } else {
                        it.bottomPlayerControlBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)

                    }
                }
            })
        }
    }

    private fun initControlBtn(fragmentPlayerBinding: FragmentPlayerBinding) {

        fragmentPlayerBinding.bottomPlayerControlBtn.setOnClickListener {
            val player = this.player ?: return@setOnClickListener    // player가 null일 경우 아무 행동 X

            // 클릭했을 때 플레이 상태에 따라 player 재생 상태 변경
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }

        }

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

    // 영상을 클릭했을 때 , Motion 상태를 end로 만들어 화면을 올리고 영상을 교체하는 함수
    fun playWithUrl(url: String, title: String) {

        context?.let {
            val dataSourceFactory = DefaultDataSourceFactory(it)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.play()
        }

        binding?.let {
            it.playerMotionLayout.transitionToEnd()    // 프래그먼트가 end상태로 변한다.
            it.bottomTitleTextView.text = title
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()    // 플레이어 일시 정지
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null    // 뷰가 파괴될때 뷰바인딩 해제
        player?.release()    // 플레이어 해제

    }


}