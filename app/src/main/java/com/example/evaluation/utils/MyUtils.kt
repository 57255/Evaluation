package com.example.evaluation.utils

import android.service.notification.NotificationListenerService
import android.widget.Toast

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}
const val TAKE_PHOTO = 1
const val SELECT_PHOTO=2
const val EVALUATION_UP=10
const val EVALUATION_DOWN=11
const val EVALUATION_SAME=12
const val EXAM_DOWN=21
var RECORD_ID=0
var PERFORMANCE_RECORD=0
var RANK=""
