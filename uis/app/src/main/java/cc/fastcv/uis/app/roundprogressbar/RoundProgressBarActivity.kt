package cc.fastcv.uis.app.roundprogressbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R

class RoundProgressBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_progress_bar)

        findViewById<RoundProgressBar>(R.id.rpb).setOnClickListener {
            (it as RoundProgressBar).run()
        }

        findViewById<LeafLoadingView>(R.id.llv).setOnClickListener {
            (it as LeafLoadingView).run()
        }
    }


}