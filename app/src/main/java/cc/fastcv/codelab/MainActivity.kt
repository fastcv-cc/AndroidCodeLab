package cc.fastcv.codelab

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.libs.app.LibsMainActivity
import cc.fastcv.uis.app.UisMainActivity

class MainActivity : TreeActivity() {
    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem(
                "轮子库集合",
                "自己实现的轮子库及其测试项",
                Intent(this, LibsMainActivity::class.java)
            ),
            LibItem(
                "UI库集合",
                "自己实现的UI库及其测试项",
                Intent(this, UisMainActivity::class.java)
            ),
        )
    }
}