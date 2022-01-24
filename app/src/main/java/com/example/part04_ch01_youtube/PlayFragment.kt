package com.example.part04_ch01_youtube

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.part04_ch01_youtube.databinding.FragmentPlayerBinding
import kotlin.math.abs

class PlayFragment : Fragment(R.layout.fragment_player) {

    private var binding: FragmentPlayerBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

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

    override fun onDestroy() {
        super.onDestroy()

        binding = null    // 뷰가 파괴될때 뷰바인딩 해제

    }


}