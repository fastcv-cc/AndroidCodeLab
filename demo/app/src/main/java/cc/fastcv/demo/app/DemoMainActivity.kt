package cc.fastcv.demo.app

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.layouts.LayoutMainActivity
import cc.fastcv.sql.SqlMainActivity

class DemoMainActivity : TreeActivity() {
    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem(
                "安卓SQL的相关使用", "安卓SQL相关的原生与三方框架的使用集合",
                Intent(this, SqlMainActivity::class.java)
            ),
            LibItem(
                "安卓Layout的相关使用", "安卓相关布局的简单使用与组合复杂使用的用法集合",
                Intent(this, LayoutMainActivity::class.java)
            ),

            )
    }
}