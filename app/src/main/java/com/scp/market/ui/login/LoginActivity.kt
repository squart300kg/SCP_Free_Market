package com.scp.market.ui.login

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.scp.market.Application
import com.scp.market.R
import com.scp.market.databinding.ActivityLoginBinding
import com.scp.market.model.login.request.AccessTokenRequest
import com.scp.market.state.NetworkState
import com.scp.market.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModel()
    val TAG = "LoginActivityLog"

    private val SMS_AUTH_REQ_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKakaoLogin.setOnClickListener {
            initKakaoLogin()
        }

        binding.btnEmailLogin.setOnClickListener {
            loginViewModel.getAccessToken(
                    AccessTokenRequest(
                            getString(R.string.imweb_API_Key),
                            getString(R.string.imweb_Secret_Key)
                    )
            )

        }

        getHashKey()

        configureObservables()

    }

    private fun configureObservables() {
        loginViewModel.accessTokenNetworkState.observe(this, Observer { networkState ->
            when (networkState) {
                NetworkState.RUNNING -> {}
                NetworkState.SUCCESS -> {
                    Log.i("액세스토큰 수신 성공", "수신 성콩!")

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                NetworkState.FAILED -> {
                    Log.i("액세스토큰 수신 나중에 성공", "나중에 성공할 것임")
                }
            }
        })
    }

    private fun initKakaoLogin() {

        // 카카오톡으로 로그인
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "카카오톡 로그인 성공 ${token.accessToken}")

                loginViewModel.getAccessToken(
                    AccessTokenRequest(
                       getString(R.string.imweb_API_Key),
                        getString(R.string.imweb_Secret_Key)
                    )
                )


            }
        }
    }


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