package cc.fastcv.uis.app.round_corner_progress_bar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R

class RoundCornerProgressBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_corner_progress_bar)

        findViewById<RoundCornerProgressBar>(R.id.rpb).setOnClickListener {
            (it as RoundCornerProgressBar).run()
        }

        findViewById<LeafLoadingView>(R.id.llv).setOnClickListener {
            (it as LeafLoadingView).run()
        }
    }


}