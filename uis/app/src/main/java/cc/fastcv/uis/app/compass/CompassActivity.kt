package cc.fastcv.uis.app.compass

import android.os.Bundle
import cc.fastcv.compass_view.CompassView
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.uis.app.R

class CompassActivity : LeafActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        val compassView = findViewById<CompassView>(R.id.compassView)
        compassView.bindLifecycle(this)
    }

}