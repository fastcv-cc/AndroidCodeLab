package cc.fastcv.uis.app.path_measure

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R
import kotlin.math.max
import kotlin.math.min

class PathMeasureActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {


    /**
     * 0 初始化
     * 1 准备中
     * 2 搜索中
     * 3 完成
     */
    private var state = 0

    /**
     * 进度
     */
    private var curProgress = 0f

    private val handler = Handler(Looper.getMainLooper())

    private var animRunning = false

    private lateinit var searchLoadingView: SearchLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_measure)
        searchLoadingView = findViewById(R.id.SearchLoadingView)

        findViewById<RadioGroup>(R.id.rg_state).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_state_idle -> {
                    state = 0
                    updateSearchLoadingView()
                }
                R.id.rb_state_prepare -> {
                    state = 1
                    updateSearchLoadingView()
                }
                R.id.rb_state_searching -> {
                    state = 2
                    updateSearchLoadingView()
                }
                R.id.rb_state_finished -> {
                    state = 3
                    updateSearchLoadingView()
                }
            }
        }

        val sbProgress = findViewById<SeekBar>(R.id.sb_progress)

        sbProgress.setOnSeekBarChangeListener(this)

        findViewById<ImageView>(R.id.add).setOnClickListener {
            sbProgress.progress = (min(100f, curProgress + 1f) * 1000).toInt()
        }

        findViewById<ImageView>(R.id.reduce).setOnClickListener {
            sbProgress.progress = (max(0f, curProgress - 1f) * 1000).toInt()
        }

        findViewById<Button>(R.id.bt).setOnClickListener {
            if (!animRunning) {
                animRunning = true
                searchLoadingView.startSearch()
                handler.postDelayed({
                    searchLoadingView.finishSearch()
                    animRunning = false
                }, 10000)
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        curProgress = (progress / 100000f) * 100f
        Log.d("xcl_debug", "onProgressChanged: curProgress = $curProgress")
        updateSearchLoadingView()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


    @SuppressLint("SetTextI18n")
    private fun updateSearchLoadingView() {
        findViewById<TextView>(R.id.value).text = "$curProgress%"

        searchLoadingView.setStateAndProgressForTest(state,curProgress)
    }

}