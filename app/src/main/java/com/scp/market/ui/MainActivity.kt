package com.scp.market.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.user.UserApiClient
import com.scp.market.R
import com.scp.market.databinding.ActivityMainBinding
import com.scp.market.ui.charge.ChargeFragment
import com.scp.market.ui.main.MainFragment
import com.scp.market.ui.myroom.MyRoomFragment
import com.scp.market.ui.register.RegisterFragment
import com.scp.market.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navigationBar: BottomNavigationView

    private var lastTimeBackPressed = 0L;

    val TAG = "MainActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationBar()

        ToastUserInfo()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            val tempTime = System.currentTimeMillis()
            val intervalTime = tempTime - lastTimeBackPressed
            if (!(0 > intervalTime || 2000L < intervalTime)) {
                finishAffinity()
                System.runFinalization()
                System.exit(0)
            } else {
                lastTimeBackPressed = tempTime
                Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            }

        }
        super.onBackPressed()
        updateBottomMenu(binding.navigationBar)
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
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}" +
                        "\n접근토근 : ${user.kakaoAccount?.phoneNumber}")

            }
        }
    }

    private fun setBottomNavigationBar() {
        navigationBar = binding.navigationBar
        supportFragmentManager.beginTransaction().replace(R.id.container , MainFragment(), "menu01").commitAllowingStateLoss()

        binding.navigationBar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu01 -> {
//                    supportFragmentManager.popBackStack("menu01", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container , MainFragment(), "menu01")
                            .addToBackStack("menu01")
                            .commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu02 -> {
//                    supportFragmentManager.popBackStack("menu02", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container , RegisterFragment(), "menu02")
                            .addToBackStack("menu02")
                            .commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu03 -> {
//                    supportFragmentManager.popBackStack("menu03", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container , SearchFragment(), "menu03")
                            .addToBackStack("menu03")
                            .commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu04 -> {
//                    supportFragmentManager.popBackStack("menu04", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container , ChargeFragment(), "menu04")
                            .addToBackStack("menu04")
                            .commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu05 -> {
//                    supportFragmentManager.popBackStack("menu05", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container , MyRoomFragment(), "menu05")
                            .addToBackStack("menu05")
//                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commitAllowingStateLoss()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun updateBottomMenu(navigation: BottomNavigationView) {
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("menu01")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("menu02")
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("menu03")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("menu04")
        val tag5: Fragment? = supportFragmentManager.findFragmentByTag("menu05")
        val tag6: Fragment? = supportFragmentManager.findFragmentByTag("localAdmin")

        if(tag1 != null && tag1.isVisible) {
            Log.i("updateBottomMenu", "1")
            navigation.menu.findItem(R.id.menu01).isChecked = true }
        if(tag2 != null && tag2.isVisible) {
            Log.i("updateBottomMenu", "2")
            navigation.menu.findItem(R.id.menu02).isChecked = true }
        if(tag3 != null && tag3.isVisible) {
            Log.i("updateBottomMenu", "3")
            navigation.menu.findItem(R.id.menu03).isChecked = true }
        if(tag4 != null && tag4.isVisible) {
            Log.i("updateBottomMenu", "4")
            navigation.menu.findItem(R.id.menu04).isChecked = true }
        if(tag5 != null && tag5.isVisible) {
            Log.i("updateBottomMenu", "5")
            navigation.menu.findItem(R.id.menu05).isChecked = true }
        if(tag6 != null && tag6.isVisible) {
            Log.i("updateBottomMenu", "6")
            navigation.menu.findItem(R.id.menu05).isChecked = true }

    }
}