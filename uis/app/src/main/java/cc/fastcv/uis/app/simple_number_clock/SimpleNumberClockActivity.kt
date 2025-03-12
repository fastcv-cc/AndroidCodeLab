package cc.fastcv.uis.app.simple_number_clock

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.simple_number_clock.SimpleClockView
import cc.fastcv.uis.app.R

class SimpleNumberClockActivity : LeafActivity() {

    private lateinit var view: SimpleClockView

    private val handler = Handler(Looper.getMainLooper())

    private val showRunnable = Runnable {
        view.showInAnim()
        showNext()
    }

    private fun showNext() {
        handler.postDelayed(showRunnable, 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_number_clock)
        view = findViewById(R.id.view)
        view.post(showRunnable)
        view.setOnClickListener {
            val isPortrait =
                resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            requestedOrientation = if (isPortrait) {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(showRunnable)
    }
}