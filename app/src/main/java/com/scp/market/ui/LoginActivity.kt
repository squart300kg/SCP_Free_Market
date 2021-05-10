package com.scp.market.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.scp.market.R
import com.scp.market.databinding.ActivityLoginBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val TAG = "LoginActivityLog"

    private val SMS_AUTH_REQ_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val list = listOf(binding.btnKakaoLogin, binding.btnPhoneLogin, binding.btnKakaoSignUp, binding.btnPhoneSignUp)
//        for (item in list) {
//            item.setOnClickListener {
//                Intent(this, MainActivity::class.java)
//                    .run { startActivity(this) }
//                finish()
//            }
//        }
        binding.btnKakaoLogin.setOnClickListener {
            initKakaoLogin()
        }

        binding.btnPhoneLogin.setOnClickListener {
            startActivity(Intent(this, SmsAuthActivity::class.java))
        }

    }

    private fun initKakaoLogin() {
        KakaoSdk.init(this, "2b0621f17b73cb652e080c089fde5d7e")
        // 카카오톡으로 로그인
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == SMS_AUTH_REQ_CODE) {
//            data?.let {
//                it.getStringExtra("result")?.let { result ->
//                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
//                }
//                it.getStringExtra("imp_key")?.let { impKey ->
//                    viewModel.smsAuthPostAccessToken(getString(R.string.imp_key), getString(R.string.imp_secret), impKey)
//                } ?: Toast.makeText(this, "인증 결과가 누락되었습니다.\n재인증이 필요합니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }
}