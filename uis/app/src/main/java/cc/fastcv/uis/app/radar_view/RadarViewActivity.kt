package cc.fastcv.uis.app.radar_view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class RadarViewActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var radarView: RadarView

    private val strengths = arrayOf(1f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radar_view)

        radarView = findViewById(R.id.RadarView)
        radarView.setStrengths(strengths.toFloatArray())

        findViewById<ImageView>(R.id.add_0).setOnClickListener(this)
        findViewById<ImageView>(R.id.add_1).setOnClickListener(this)
        findViewById<ImageView>(R.id.add_2).setOnClickListener(this)
        findViewById<ImageView>(R.id.add_3).setOnClickListener(this)
        findViewById<ImageView>(R.id.add_4).setOnClickListener(this)
        findViewById<ImageView>(R.id.add_5).setOnClickListener(this)
        findViewById<ImageView>(R.id.reduce_0).setOnClickListener(this)
        findViewById<ImageView>(R.id.reduce_1).setOnClickListener(this)
        findViewById<ImageView>(R.id.reduce_2).setOnClickListener(this)
        findViewById<ImageView>(R.id.reduce_3).setOnClickListener(this)
        findViewById<ImageView>(R.id.reduce_4).setOnClickListener(this)
        findViewById<ImageView>(R.id.reduce_5).setOnClickListener(this)
        updateValueUI()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.add_0 -> {
                strengths[0] = min(1.0f,strengths[0] + 0.1f)
            }
            R.id.add_1 -> {
                strengths[1] = min(1.0f,strengths[1] + 0.1f)
            }
            R.id.add_2 -> {
                strengths[2] = min(1.0f,strengths[2] + 0.1f)
            }
            R.id.add_3 -> {
                strengths[3] = min(1.0f,strengths[3] + 0.1f)
            }
            R.id.add_4 -> {
                strengths[4] = min(1.0f,strengths[4] + 0.1f)
            }
            R.id.add_5 -> {
                strengths[5] = min(1.0f,strengths[5] + 0.1f)
            }
            R.id.reduce_0 -> {
                strengths[0] = max(0.0f,strengths[0] - 0.1f)
            }
            R.id.reduce_1 -> {
                strengths[1] = max(0.0f,strengths[1] - 0.1f)
            }
            R.id.reduce_2 -> {
                strengths[2] = max(0.0f,strengths[2] - 0.1f)
            }
            R.id.reduce_3 -> {
                strengths[3] = max(0.0f,strengths[3] - 0.1f)
            }
            R.id.reduce_4 -> {
                strengths[4] = max(0.0f,strengths[4] - 0.1f)
            }
            R.id.reduce_5 -> {
                strengths[5] = max(0.0f,strengths[5] - 0.1f)
            }
            else -> {}
        }
        updateValueUI()
        radarView.setStrengths(strengths.toFloatArray())
    }

    @SuppressLint("SetTextI18n")
    private fun updateValueUI() {
        findViewById<TextView>(R.id.value_0).text = "${(strengths[0]*100f).roundToInt()}%"
        findViewById<TextView>(R.id.value_1).text = "${(strengths[1]*100f).roundToInt()}%"
        findViewById<TextView>(R.id.value_2).text = "${(strengths[2]*100f).roundToInt()}%"
        findViewById<TextView>(R.id.value_3).text = "${(strengths[3]*100f).roundToInt()}%"
        findViewById<TextView>(R.id.value_4).text = "${(strengths[4]*100f).roundToInt()}%"
        findViewById<TextView>(R.id.value_5).text = "${(strengths[5]*100f).roundToInt()}%"
    }

}