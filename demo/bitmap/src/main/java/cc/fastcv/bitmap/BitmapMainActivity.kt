package cc.fastcv.bitmap

import android.content.Intent
import cc.fastcv.bitmap.puzzle.PuzzleActivity
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity

class BitmapMainActivity : TreeActivity() {
    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem("拼图游戏", "拼图游戏", Intent(this, PuzzleActivity::class.java))
        )
    }
}