package cc.fastcv.uis.app.inscribed_circle

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R

class InscribedCircleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscribed_circle)

        val bt = findViewById<Button>(R.id.bt)
        val ic = findViewById<InscribedCircleView>(R.id.ic)
        val etL = findViewById<EditText>(R.id.etL)
        val etN = findViewById<EditText>(R.id.etN)

        bt.setOnClickListener {
            val L = etL.text.toString().toInt()
            val N = etN.text.toString().toInt()
            ic.setLAndN(L, N)
        }
    }

}