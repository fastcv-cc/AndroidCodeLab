package cc.fastcv.libs.app.file_manager.media_store

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity

class MediaStoreActivity : TreeActivity() {

    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem(
                "MediaStore.Images",
                "MediaStore.Images的相关用法",
                Intent(this, MediaStoreImagesActivity::class.java)
            ),
            LibItem(
                "MediaStore.Video",
                "MediaStore.Video的相关用法",
                Intent(this, MediaStoreVideoActivity::class.java)
            ),
            LibItem(
                "MediaStore.Audio",
                "MediaStore.Audio的相关用法",
                Intent(this, MediaStoreAudioActivity::class.java)
            ),
            LibItem(
                "MediaStore.Downloads",
                "MediaStore.Downloads的相关用法",
                Intent(this, MediaStoreDownloadsActivity::class.java)
            ),
            LibItem(
                "MediaStore.File",
                "MediaStore.File的相关用法",
                Intent(this, MediaStoreFileActivity::class.java)
            ),
        )
    }
}