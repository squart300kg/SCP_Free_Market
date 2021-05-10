package com.scp.market.util

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.webkit.JavascriptInterface

class SmsAuthFactory(activity: Activity) {

    private val TAG = "SmsAuthFactory★"
    private val activity = activity

    @JavascriptInterface
    fun resultAuth(message: String, impKey: String?) {
        //javascript로 부터 온 parameter (message)를 처리
        val intent = Intent()

        if(message == "success" && impKey != null) {
            intent.putExtra("result","success")
            intent.putExtra("imp_key", impKey)
            activity.setResult(RESULT_OK,intent)
            activity.finish()
        }
        else {
            intent.putExtra("result","failure")
            activity.setResult(RESULT_OK,intent)
            activity.finish()
        }
    }
}