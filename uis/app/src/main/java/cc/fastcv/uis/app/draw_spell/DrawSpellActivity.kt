package cc.fastcv.uis.app.draw_spell

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R

class DrawSpellActivity : AppCompatActivity(), TextWatcher, SeekBar.OnSeekBarChangeListener {

    private lateinit var dsv: DrawSpellView
    private lateinit var etColor: EditText

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_spell)

        dsv = findViewById(R.id.dsv)

        findViewById<LinearLayout>(R.id.ll_bottom_sheet).setOnClickListener { }

        findViewById<Button>(R.id.bt_reset).setOnClickListener {
            dsv.reset()
        }

        etColor = findViewById(R.id.et_color)
        etColor.addTextChangedListener(this)
        etColor.setText("#AAA99A")

        findViewById<SeekBar>(R.id.sb_color).apply {
            max = 0xFFFFFF
            progress = 0xf4ea2a
            setOnSeekBarChangeListener(this@DrawSpellActivity)
        }

        initShader()
        initTileMode()
        initBitmapSelect()
    }

    private var ivIndex = 0

    private fun initBitmapSelect() {

        findViewById<ImageView>(R.id.iv_16).setOnClickListener {
            updateIvSelect(0)
        }

        findViewById<ImageView>(R.id.iv_32).setOnClickListener {
            updateIvSelect(1)
        }

        findViewById<ImageView>(R.id.iv_64).setOnClickListener {
            updateIvSelect(2)
        }

        findViewById<ImageView>(R.id.iv_128).setOnClickListener {
            updateIvSelect(3)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ObsoleteSdkInt")
    private fun updateIvSelect(index: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (ivIndex) {
                0 -> {
                    findViewById<ImageView>(R.id.iv_16).foreground = null
                }

                1 -> {
                    findViewById<ImageView>(R.id.iv_32).foreground = null
                }

                2 -> {
                    findViewById<ImageView>(R.id.iv_64).foreground = null
                }

                3 -> {
                    findViewById<ImageView>(R.id.iv_128).foreground = null
                }

                else -> {}
            }

            when (index) {
                0 -> {
                    findViewById<ImageView>(R.id.iv_16).foreground =
                        getDrawable(R.drawable.shape_bitmap_selected_bg)
                }

                1 -> {
                    findViewById<ImageView>(R.id.iv_32).foreground =
                        getDrawable(R.drawable.shape_bitmap_selected_bg)
                }

                2 -> {
                    findViewById<ImageView>(R.id.iv_64).foreground =
                        getDrawable(R.drawable.shape_bitmap_selected_bg)
                }

                3 -> {
                    findViewById<ImageView>(R.id.iv_128).foreground =
                        getDrawable(R.drawable.shape_bitmap_selected_bg)
                }

                else -> {}
            }
            ivIndex = index
            dsv.setBitmapSize((ivIndex + 1) * 16.0f)
        }
    }

    private fun initTileMode() {
        findViewById<RadioGroup>(R.id.rg_tile_mode).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_clamp -> {
                    dsv.setTileMode(0)
                }

                R.id.rb_mirror -> {
                    dsv.setTileMode(1)
                }

                R.id.rb_repeat -> {
                    dsv.setTileMode(2)
                }
            }
        }
    }

    private fun initShader() {
        findViewById<RadioGroup>(R.id.rg_shader).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_linear -> {
                    dsv.setShader(0)
                }

                R.id.rb_radial -> {
                    dsv.setShader(1)
                }

                R.id.rb_sweep -> {
                    dsv.setShader(2)
                }

                R.id.rb_Bitmap -> {
                    dsv.setShader(3)
                }

                R.id.rb_compose -> {
                    dsv.setShader(4)
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length == 7 && s.startsWith("#")) {
            try {
                val parseColor = Color.parseColor(s.toString())
                dsv.setBackgroundColor(parseColor)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    @SuppressLint("SetTextI18n")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        etColor.setText(String.format("#%06X", progress))
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}