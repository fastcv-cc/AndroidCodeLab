package cc.fastcv.uis.app.round_progress_bar

import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.round_progress_bar.RoundProgressBar
import cc.fastcv.uis.app.R

class RoundProgressBarActivity : AppCompatActivity() {

    private lateinit var rpb: RoundProgressBar
    private lateinit var sbStartColor: SeekBar
    private lateinit var sbEndColor: SeekBar
    private lateinit var cb: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_progress_bar)
        rpb = findViewById(R.id.rpb)

        findViewById<SeekBar>(R.id.sbWidth).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    rpb.setProgressWidth(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        findViewById<SeekBar>(R.id.sbProgress).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    rpb.setProgress(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        findViewById<SeekBar>(R.id.sbProgressColor).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    rpb.setProgressColor(
                        getColorForProgress(
                            progress / 100f,
                            Color.RED,
                            Color.GREEN
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        findViewById<SeekBar>(R.id.sbBgColor).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    rpb.setProgressBgColor(
                        getColorForProgress(
                            progress / 100f,
                            Color.GREEN,
                            Color.BLUE
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }


        sbStartColor = findViewById(R.id.sbStartColor)
        sbEndColor = findViewById(R.id.sbEndColor)
        sbStartColor.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                updateGradientColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        sbEndColor.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                updateGradientColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        cb = findViewById(R.id.cb)
        cb.setOnCheckedChangeListener { _, _ ->
            updateGradientColor()
        }

    }

    private fun updateGradientColor() {
        if (findViewById<CheckBox>(R.id.cb).isChecked) {
            rpb.setGradientColor(
                getColorForProgress(
                    sbStartColor.progress / 100f,
                    Color.GREEN,
                    Color.BLUE
                ), getColorForProgress(
                    sbEndColor.progress / 100f,
                    Color.GREEN,
                    Color.BLUE
                )
            )
        } else {
            rpb.setGradientColor(0, 0)
        }
    }

    fun getColorForProgress(progress: Float, startColor: Int, endColor: Int): Int {
        // 确保进度在 [0, 1] 范围内
        val clampedProgress = progress.coerceIn(0f, 1f)

        // 提取 RGB 分量
        val startRed = Color.red(startColor)
        val startGreen = Color.green(startColor)
        val startBlue = Color.blue(startColor)

        val endRed = Color.red(endColor)
        val endGreen = Color.green(endColor)
        val endBlue = Color.blue(endColor)

        // 通过进度值计算每个颜色分量的插值
        val red = interpolate(startRed, endRed, clampedProgress)
        val green = interpolate(startGreen, endGreen, clampedProgress)
        val blue = interpolate(startBlue, endBlue, clampedProgress)

        // 返回插值后的颜色
        return Color.rgb(red, green, blue)
    }

    // 插值函数，计算起始值到结束值之间的颜色分量
    private fun interpolate(startValue: Int, endValue: Int, progress: Float): Int {
        return (startValue + (endValue - startValue) * progress).toInt()
    }
}