package cc.fastcv.uis.app

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.uis.app.compass.CompassActivity
import cc.fastcv.uis.app.simple_number_clock.SimpleNumberClockActivity
import cc.fastcv.uis.app.theoretical_basis.TheoreticalBasisActivity

class UisMainActivity : TreeActivity() {

    override fun buildLibItemList(): List<LibItem> {

        return listOf(
            LibItem(
                "UI理论知识",
                "自定义UI必备理论知识",
                Intent(this, TheoreticalBasisActivity::class.java)
            ),LibItem(
                "极简数字时钟",
                "很好看的线段时钟",
                Intent(this, SimpleNumberClockActivity::class.java)
            ),LibItem(
                "指南针",
                "简约指南针效果，配合手机地磁",
                Intent(this, CompassActivity::class.java)
            )
        )
    }
}