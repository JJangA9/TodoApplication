package com.todo.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri.encode
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Base64.encode
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.Utility.getPackageInfo
import com.todo.myapplication.RoomDB.ScheduleDB.Companion.getInstance
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception
import java.net.URLEncoder.encode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.Calendar.getInstance

class SplashActivity : AppCompatActivity(){
    private var callback: SessionCallback = SessionCallback()

    private val WRITING_TIME_OUT:Long = 500

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstances: Bundle?) {
        super.onCreate(savedInstances)
        setContentView(R.layout.activity_splash)

        val imageView2 = findViewById<ImageView>(R.id.splashW2)
        imageView2.visibility = View.INVISIBLE

        //투두의 빨간색 글씨 1초후 나타내기
        Handler().postDelayed({
                imageView2.visibility = View.VISIBLE
            }, WRITING_TIME_OUT)

        Session.getCurrentSession().addCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback: ISessionCallback {
        override fun onSessionOpened() {
            // 로그인 세션이 열렸을 때
            UserManagement.getInstance().me( object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    // 로그인이 성공했을 때
                    var intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("name", result!!.getNickname())
                    intent.putExtra("profile", result!!.getProfileImagePath())
                    startActivity(intent)
                    finish()
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                    Toast.makeText(
                            this@SplashActivity,
                            "세션이 닫혔습니다. 다시 시도해주세요 : ${errorResult.toString()}",
                            Toast.LENGTH_SHORT).show()
                }
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            //로그인 세션이 정장적으로 열리지 않았을 때
            if(exception != null) {
                com.kakao.util.helper.log.Logger.e(exception)
                Toast.makeText(
                        this@SplashActivity,
                        "로그인 도중 오류가 발생했습ㄴ디ㅏ. 인터넷 연결을 확인해주세요 : $exception",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

}