package com.example.testcounter.utils

import android.view.MotionEvent
import com.google.android.material.textfield.TextInputEditText

const val DRAWABLE_RIGHT = 2

fun handleItemSearch(event: MotionEvent, view: TextInputEditText, exec: (e: MotionEvent, view: TextInputEditText) -> Boolean): Boolean {
    if(event.action == MotionEvent.ACTION_UP) {
        return exec(event, view)
    }
    return false
}

fun TextInputEditText.handleItemSearch(f: (e: MotionEvent, view: TextInputEditText) -> Boolean) {
    setOnTouchListener{ _, motionEvent -> handleItemSearch(motionEvent, this,
        f)}
}