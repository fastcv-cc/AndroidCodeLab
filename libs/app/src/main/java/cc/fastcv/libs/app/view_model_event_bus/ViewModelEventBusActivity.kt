package cc.fastcv.libs.app.view_model_event_bus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.libs.app.R

class ViewModelEventBusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_event_bus)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            startActivity(Intent(this, LifecycleActivity::class.java))
        }
    }

}