package cc.fastcv.libs.app.view_model_event_bus

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.repeatOnLifecycle
import cc.fastcv.libs.app.R
import cc.fastcv.logger.FastLogger


private const val TAG = "LifecycleActivity"

class LifecycleActivity : AppCompatActivity() {

    init {
        printCurrentLifecycleStates("init")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        printCurrentLifecycleStates("onPostCreate")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)
        printCurrentLifecycleStates("onCreate")


        val viewModel: LifecycleViewModel  = ViewModelProvider(this).get(LifecycleViewModel::class.java)
        Log.d(TAG, "onCreate: $viewModel")
        viewModel.getContent()
        viewModel.saveContent()

        lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                printCurrentLifecycleStates("DefaultLifecycleObserver onCreate")
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                printCurrentLifecycleStates("DefaultLifecycleObserver onStart")
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                printCurrentLifecycleStates("DefaultLifecycleObserver onResume")
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                printCurrentLifecycleStates("DefaultLifecycleObserver onPause")
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                printCurrentLifecycleStates("DefaultLifecycleObserver onStop")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                printCurrentLifecycleStates("DefaultLifecycleObserver onDestroy")
            }

        })

        findViewById<Button>(R.id.bt1).setOnClickListener {
            printCurrentLifecycleStates("click")
        }


        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {

        }
    }


    override fun onPostResume() {
        super.onPostResume()
        printCurrentLifecycleStates("onPostResume")
    }


    override fun onStart() {
        super.onStart()
        printCurrentLifecycleStates("onStart")
    }

    override fun onPause() {
        super.onPause()
        printCurrentLifecycleStates("onPause")
    }

    override fun onStop() {
        super.onStop()
        printCurrentLifecycleStates("onStop")
    }

    override fun onResume() {
        super.onResume()
        printCurrentLifecycleStates("onResume")
    }

    override fun onRestart() {
        super.onRestart()
        printCurrentLifecycleStates("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        printCurrentLifecycleStates("onDestroy")
    }

    private fun printCurrentLifecycleStates(stage: String) {
        FastLogger.i(TAG, "$stage 当前Lifecycle组件状态：${lifecycle.currentState.name}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        printCurrentLifecycleStates("onSaveInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        printCurrentLifecycleStates("onSaveInstanceState2")
    }

}