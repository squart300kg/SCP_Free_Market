package com.scp.market.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.user.UserApiClient
import com.scp.market.R
import com.scp.market.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navigationBar: BottomNavigationView

    val TAG = "MainActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationBar()

        ToastUserInfo()
    }

    private fun ToastUserInfo() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
        }
    }

    private fun setBottomNavigationBar() {
        navigationBar = binding.navigationBar
        supportFragmentManager.beginTransaction().replace(R.id.container , MainFragment()).commitAllowingStateLoss()

        binding.navigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu01 -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.container , MainFragment()).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu02 -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.container , RegisterFragment()).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu03 -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.container , SearchFragment()).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu04 -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.container , ChargeFragment()).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu05 -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.container , MyRoomFragment()).commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}