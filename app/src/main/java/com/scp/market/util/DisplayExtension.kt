package com.scp.market.util

import android.content.res.Resources

fun Int.pxToDp() : Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx() : Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.pxToSp() : Int = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()

fun Int.spToPx() : Int = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()