package cc.fastcv.uis.app.turntable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R

class TurntableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turntable)

        findViewById<TurntableView>(R.id.tbv).setOnClickListener {
//            (it as TurntableView).run()
        }
    }

}