package cc.fastcv.uis.app.theoretical_basis

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import cc.fastcv.uis.app.R
import cc.fastcv.uis.app.theoretical_basis.custom.AngleCalcView
import cc.fastcv.uis.app.theoretical_basis.custom.ClickShowView
import cc.fastcv.uis.app.theoretical_basis.custom.PressableTextView
import cc.fastcv.uis.app.theoretical_basis.custom.SimpleSpeedometer
import kotlin.math.PI


class TheoreticalBasisActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TheoreticalBasis"
    }

    private var initStatusBarColor = 0
    private var initNavigationBarColor = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theoretical_basis)

        val tvPositionInScreen = findViewById<TextView>(R.id.tv_position_in_screen)
        val tvPositionInView = findViewById<TextView>(R.id.tv_position_in_view)

        findViewById<ClickShowView>(R.id.csv).setOnClickPointerCallback(object :
            ClickShowView.OnClickPointerCallback {
            @SuppressLint("SetTextI18n")
            override fun onView(x: Float, y: Float) {
                tvPositionInView.text = "(${x.toInt()},${y.toInt()})"
            }

            @SuppressLint("SetTextI18n")
            override fun onScreen(x: Float, y: Float) {
                tvPositionInScreen.text = "(${x.toInt()},${y.toInt()})"
            }
        })

        findViewById<PressableTextView>(R.id.tv_show_statusbar).setCallback(object :
            PressableTextView.OnPressedCallback {
            override fun onPressed(pressed: Boolean) {
                if (pressed) {
                    setStatusBarColor(this@TheoreticalBasisActivity, Color.RED)
                } else {
                    if (initStatusBarColor != 0) {
                        setStatusBarColor(this@TheoreticalBasisActivity, initStatusBarColor)
                    }
                }
            }
        })

        findViewById<PressableTextView>(R.id.tv_show_toolbar).setCallback(object :
            PressableTextView.OnPressedCallback {
            override fun onPressed(pressed: Boolean) {
                if (pressed) {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.RED))
                } else {
                    supportActionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            Color.parseColor("#FF6200EE")
                        )
                    )
                }
            }
        })

        findViewById<PressableTextView>(R.id.tv_show_content).setCallback(object :
            PressableTextView.OnPressedCallback {
            override fun onPressed(pressed: Boolean) {
                if (pressed) {
                    findViewById<NestedScrollView>(R.id.scroll_view).setBackgroundColor(Color.RED)
                } else {
                    findViewById<NestedScrollView>(R.id.scroll_view).setBackgroundColor(Color.WHITE)
                }
            }
        })

        findViewById<PressableTextView>(R.id.tv_show_navigationbar).setCallback(object :
            PressableTextView.OnPressedCallback {
            override fun onPressed(pressed: Boolean) {
                if (pressed) {
                    if (initNavigationBarColor == 0) {
                        initNavigationBarColor = window.navigationBarColor
                    }
                    window.navigationBarColor = Color.RED
                } else {
                    window.navigationBarColor = initNavigationBarColor
                }

            }
        })

        findViewById<AngleCalcView>(R.id.angle_calc).setCallback(object :
            AngleCalcView.OnAngleChangeCallback {
            @SuppressLint("SetTextI18n")
            override fun onAngleChange(angle: Float) {
                findViewById<TextView>(R.id.tv_angle).text = "$angle°"
                findViewById<TextView>(R.id.tv_rag).text = "${angle * (PI / 180.0f)}"
            }
        })

        window.decorView.post {
            showStatusBarSize()
            showToolbarSize()
            showNavigationSize()
            showContentSize()
            showDisplaySize()
        }

        val displayMetrics = resources.displayMetrics
        findViewById<TextView>(R.id.tv_density).text =
            "当前设备像素密度：${displayMetrics.densityDpi} - ${displayMetrics.density}"
        findViewById<TextView>(R.id.tv_scaledDensity).text =
            "当前设备缩放密度：${displayMetrics.scaledDensity}"
        findViewById<TextView>(R.id.tv_dpi).text =
            "1dp = ${
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1.0f,
                    displayMetrics
                )
            }px"
        findViewById<TextView>(R.id.tv_sp).text =
            "1sp = ${TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1.0f, displayMetrics)}px"
        findViewById<TextView>(R.id.tv_pt).text =
            "1pt = ${TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, 1.0f, displayMetrics)}px"

        findViewById<TextView>(R.id.tv_min_distance).text =
            "当前设备最小滑动距离为：${ViewConfiguration.get(this).scaledTouchSlop}"

        findViewById<SimpleSpeedometer>(R.id.ss_slide).setCallback(object :
            SimpleSpeedometer.OnSpeedChangeCallback {
            override fun onSpeedChangeCallback(speed: Float) {
                findViewById<TextView>(R.id.tv_speed).text = "当前速度为：$speed"
            }
        })

        findViewById<TextView>(R.id.tv_hs).apply {
            text = "$isHardwareAccelerated"
        }
    }


    fun setStatusBarColor(activity: Activity, statusColor: Int) {
        val window: Window = activity.window
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (initStatusBarColor == 0) {
            initStatusBarColor = window.statusBarColor
        }
        //设置状态栏颜色
        window.statusBarColor = statusColor
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        //让view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
        val mChildView: View? = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    @SuppressLint("SetTextI18n")
    fun showDisplaySize() {
        val displayMetrics = resources.displayMetrics
        findViewById<TextView>(R.id.tv_display_size).text =
            "${displayMetrics.widthPixels} * ${displayMetrics.heightPixels}"
    }

    @SuppressLint("SetTextI18n")
    fun showStatusBarSize() {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        findViewById<TextView>(R.id.tv_statusbar_size).text =
            "${resources.displayMetrics.widthPixels} * $statusBarHeight"
    }

    @SuppressLint("SetTextI18n")
    fun showNavigationSize() {
        var navHeight = 0
        val navIsShow = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        if (navIsShow != 0) {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            navHeight = resources.getDimensionPixelSize(resourceId)
        }
        findViewById<TextView>(R.id.tv_navigationbar_size).text =
            "${resources.displayMetrics.widthPixels} * $navHeight"
    }

    @SuppressLint("SetTextI18n")
    fun showContentSize() {
        findViewById<TextView>(R.id.tv_content_size).text =
            "${resources.displayMetrics.widthPixels} * ${findViewById<NestedScrollView>(R.id.scroll_view).height}"
    }

    @SuppressLint("SetTextI18n")
    fun showToolbarSize() {
        findViewById<TextView>(R.id.tv_toolbar_size).text =
            "${resources.displayMetrics.widthPixels} * ${supportActionBar?.height}"

    }
}