package cc.fastcv.libs.app.file_manager

import android.content.Intent
import cc.fastcv.libs.app.file_manager.internal_storage.InternalStorageActivity
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.libs.app.file_manager.external_private_storage.ExternalPrivateStorageActivity
import cc.fastcv.libs.app.file_manager.external_public_storage.ExternalPublicStorageActivity
import cc.fastcv.libs.app.file_manager.media_store.MediaStoreActivity
import cc.fastcv.libs.app.file_manager.saf.SAFActivity

class FileManagerMainActivity : TreeActivity() {

    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem(
                "请求访问App内部存储",
                "安卓所有版本访问此目录都不需要申请权限，此目录内的所有内容会随着app的卸载被删除",
                Intent(this, InternalStorageActivity::class.java)
            ),
            LibItem(
                "请求访问外部存储私有目录",
                "安卓4.4及以上版本访问此目录都不需要申请权限，此目录内的所有内容会随着app的卸载被删除，但是需要注意可能不存在外部存储",
                Intent(
                    this, ExternalPrivateStorageActivity::class.java
                )
            ),
            LibItem(
                "请求访问外部存储公共目录", "不同版本需要申请的不同的权限", Intent(
                    this, ExternalPublicStorageActivity::class.java
                )
            ),
            LibItem(
                "SAF",
                "Android 4.4（API 级别 19）引入了存储访问框架 (SAF)，基于内容提供者。无需权限。",
                Intent(this, SAFActivity::class.java)
            ),
            LibItem(
                "MediaStore",
                "官方提供的媒体库，基于内容提供者。不同版本需要申请的不同的权限。",
                Intent(this, MediaStoreActivity::class.java)
            ),
        )
    }
}