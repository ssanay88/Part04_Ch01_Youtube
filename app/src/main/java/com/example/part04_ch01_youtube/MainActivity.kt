package com.example.part04_ch01_youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayFragment())
            .commit()

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