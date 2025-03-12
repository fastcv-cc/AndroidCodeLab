package cc.fastcv.uis.app

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.uis.app.simple_number_clock.SimpleNumberClockActivity

class UisMainActivity : TreeActivity() {

    override fun buildLibItemList(): List<LibItem> {

        return listOf(
            LibItem(
                "极简数字时钟",
                "很好看的线段时钟",
                Intent(this, SimpleNumberClockActivity::class.java)
            )
        )
    }
}