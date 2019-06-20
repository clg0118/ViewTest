package com.clg.viewtest.utils

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)