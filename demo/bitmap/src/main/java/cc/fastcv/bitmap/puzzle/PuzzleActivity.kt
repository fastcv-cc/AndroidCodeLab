package cc.fastcv.bitmap.puzzle

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import cc.fastcv.bitmap.R
import cc.fastcv.lab_base.LeafActivity
import java.util.Random

class PuzzleActivity : LeafActivity(), View.OnTouchListener {

    private lateinit var img: ImageView

    private var row = 3
    private var col = 3
    private lateinit var srcBitmap: Bitmap
    private var width: Int = 0
    private var height: Int = 0
    private var chosenNum = -1
    private var misCount = -1
    private var numArr: IntArray = intArrayOf()
    private var picArr: Array<Bitmap?> = arrayOf()
    private var newBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        img = findViewById(R.id.img_game)
        srcBitmap = img.drawable.toBitmap()

        val btnStart = findViewById<TextView>(R.id.btnStart)
        btnStart.setOnClickListener {
            initSourceBitmap()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSourceBitmap() {
        numArr = randArray(row * col)
        misCount = misCount(numArr, row * col)
        picArr = arrayOfNulls(row * col)
        width = img.width
        height = img.height
        newBitmap = centerCrop(srcBitmap, width, height)
        val tmpHeight: Int = newBitmap!!.getHeight()
        val tmpWidth: Int = newBitmap!!.getWidth()
        for (i in 0 until row) {
            for (j in 0 until col) {
                val pos = i * row + j
                val srcRow: Int = numArr[pos] / row
                val srcCol: Int = numArr[pos] % col
                picArr[pos] = Bitmap.createBitmap(
                    newBitmap!!,
                    srcCol * (tmpWidth / col),
                    srcRow * (tmpHeight / row),
                    tmpWidth / col,
                    tmpHeight / row
                )
            }
        }
        val toDraw = Canvas(newBitmap!!)
        for (i in 0 until row) for (j in 0 until col) picArr.get(i * row + j)?.let {
            toDraw.drawBitmap(
                it, (j * (tmpWidth / col)).toFloat(), (i * (tmpHeight / row)).toFloat(), null
            )
        }
        toDraw.save()
        toDraw.restore()
        img.setImageBitmap(newBitmap)
        img.setOnTouchListener(this)
    }


    private fun centerCrop(src: Bitmap, W: Int, H: Int): Bitmap {
        val w = src.width
        val h = src.height
        val ratio = W.toFloat() / H
        val dst: Bitmap = if ((w.toFloat() / h) > ratio) { //crop width
            Bitmap.createBitmap(
                srcBitmap, ((w - ratio * h) / 2).toInt(), 0, (ratio * h).toInt(), h
            )
        } else { //crop height
            Bitmap.createBitmap(
                srcBitmap, 0, ((h - w / ratio) / 2).toInt(), w, (w / ratio).toInt()
            )
        }
        return dst
    }

    private fun misCount(arr: IntArray, len: Int): Int {
        var tmp = 0
        for (i in 0 until len) {
            if (arr[i] != i) tmp++
        }
        return tmp
    }

    private fun randArray(len: Int): IntArray {
        val arr = IntArray(len)
        val status = BooleanArray(len)
        val rand = Random()
        var c = 0
        while (c < len) {
            val tmp = rand.nextInt(len)
            if (!status[tmp]) {
                arr[c] = tmp
                status[tmp] = true
                c++
            }
        }
        return arr
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x: Float = event.x //get clicked x position
        val y: Float = event.y //get clicked y position
        Log.d("xcl_debug", "onTouch: width = $width   height = $height")
        Log.d("xcl_debug", "onTouch: x = $x   y = $y")
        val tmpCol: Int = (x / (width / col)).toInt()
        val tmpRow: Int = (y / (height / row)).toInt()
        Log.d("xcl_debug", "onTouch: tmpCol = $tmpCol   tmpRow = $tmpRow")
        val newChosenNum = tmpRow * col + tmpCol
        Log.d("xcl_debug", "onTouch: chosenNum = $chosenNum   newChosenNum = $newChosenNum")
        if ((chosenNum != -1) && (newChosenNum != chosenNum)) { //do swap
            swapBlock(chosenNum, newChosenNum)
            chosenNum = -1
            if (misCount == 0) { //Game Win!
                Toast.makeText(this, "通关了！！！", Toast.LENGTH_SHORT).show()
            }
        } else { //set one chosen
            chosenNum = newChosenNum
        }
        return false
    }

    private fun swapBlock(a: Int, b: Int) {
        if (a == b) return
        var x = 0
        var y = 0
        if (numArr[a] == a) x++
        if (numArr[b] == b) x++
        if (a == numArr[b]) y++
        if (b == numArr[a]) y++
        misCount -= (y - x)
        //swap values
        val iTmp: Int = numArr[a]
        numArr[a] = numArr[b]
        numArr[b] = iTmp
        val bTmp: Bitmap = Bitmap.createBitmap(picArr[a]!!)
        picArr[a] = Bitmap.createBitmap(picArr[b]!!)
        picArr[b] = Bitmap.createBitmap(bTmp)
        bTmp.recycle()
        //draw effect
        val toDraw = Canvas(newBitmap!!)
        toDraw.drawBitmap(
            picArr[a]!!,
            ((a % row) * (newBitmap!!.getWidth() / col)).toFloat(),
            ((a / row) * (newBitmap!!.getHeight() / row)).toFloat(),
            null
        )
        toDraw.drawBitmap(
            picArr[b]!!,
            ((b % row) * (newBitmap!!.getWidth() / col)).toFloat(),
            ((b / row) * (newBitmap!!.getHeight() / row)).toFloat(),
            null
        )
        toDraw.save()
        toDraw.restore()
        img.setImageBitmap(newBitmap)
    }
}