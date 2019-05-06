package com.g00fy2.clearableedittextsample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clearableedittext.onClearIconTouchListener = View.OnTouchListener { _, _ ->
            Log.d(localClassName, "Clear pressed")
            true
        }
    }
}
