package com.example.part04_ch01_youtube

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

// attrtibuteSet을 nullable하게 하는 이유 : 상속받는 MotionLayout에서 attributeSet을 필요로 할 수도 있고 안쓰일 수도 있다.
// fragment_player 화면을 Custom한 레이아웃으로 설정하여 터치 설정
class VideoMotionLayout(context: Context, attributeSet: AttributeSet? = null): MotionLayout(context,attributeSet) {

    private var motionTouchStarted = false  // 이 값이 지정한 영역을 터치했을 경우만 True로 지정하여 터치되도록 한다.
    // mainContainerLayout만 건드는 경우 motionTouchStarted 설정
    private val mainContainerView by lazy {
        findViewById<View>(R.id.mainContainerLayout)
    }
    private val hitRect = Rect()    // 사각형을 만드는 클래스

    init {
        setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?, startId: Int, endId: Int
            ) { }

            override fun onTransitionChange(motionLayout: MotionLayout?,startId: Int,endId: Int,progress: Float) { }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                motionTouchStarted = false
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

    // 터치가 mainContainerView안에서 일어나는지 확인
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchStarted = false    // 터치를 때거나 취소하는 경우는 아무 행동도 취하지 않는다.
                return super.onTouchEvent(event)
            }
        }

        // 터치시 motionTouchStarted가 false인 경우, 정해진 영역안에 터치가 일어났는지 확인
        if (!motionTouchStarted) {
            mainContainerView.getHitRect(hitRect)    // mainContainerView영역을 히트박스 설정
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())    // 클릭한 좌표가 히트 박스 안에 있는지
        }

        return super.onTouchEvent(event) && motionTouchStarted    // motiontouch를 시작할지 반환

    }

    // 스크롤 제스쳐에 관한 리스너를 선언
    private val gestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent,e2: MotionEvent,distanceX: Float,distanceY: Float): Boolean {
                // 스크롤이 mainContainerView에서 일어나는지 확인
                mainContainerView.getHitRect(hitRect)
                return hitRect.contains(e1.x.toInt(), e1.y.toInt())
            }
        }
    }

    //
    private val gestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }

    //
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }



}