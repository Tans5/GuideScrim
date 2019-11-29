package com.tans.guidescrim.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.tans.guidescrim.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scrim_view.setOnTouchListener { v, event ->
            val action = event.action
            if (action == MotionEvent.ACTION_UP) {
                println("press: x=${event.x}, y=${event.y}")
            }
            false
        }
    }
}
