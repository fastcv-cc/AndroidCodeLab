package cc.fastcv.libs.app.file_manager.media_store

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import cc.fastcv.file_manager.FileListManagerActivity
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MediaStoreImagesActivity : LeafActivity() {

    private val takePicture = ActivityResultContracts.TakePicture()

    private val openGalleryContact = ActivityResultContracts.StartActivityForResult()

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_store_images)

        //生成uri
        val takePictureUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this,
                "cc.fastcv.libs.app.file_manager.fileprovider",
                File(externalCacheDir, "avatar.png")
            )
        } else {
            Uri.fromFile(File(externalCacheDir, "avatar.png"))
        }

        val openGalleryResult = registerForActivityResult(
            openGalleryContact
        ) { result: ActivityResult ->
            var uri: Uri? = null
            if (result.data != null) {
                uri = result.data!!.data
            }
            if (uri != null) {
                FileListManagerActivity.intoActivity(
                    this@MediaStoreImagesActivity,
                    arrayListOf(uri)
                )
            }
        }

        val cameraContact = registerForActivityResult(takePicture) { result: Boolean ->
            if (result) {
                FileListManagerActivity.intoActivity(
                    this@MediaStoreImagesActivity,
                    arrayListOf(takePictureUri)
                )
            }
        }

        findViewById<Button>(R.id.bt1).setOnClickListener {
            cameraContact.launch(
                takePictureUri
            )
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            openGalleryResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            loadImages()
        }

    }

    private fun loadImages() {
        object : Thread() {
            override fun run() {
                try {
                    val imageList: ArrayList<Uri> = queryImages()
                    runOnUiThread {
                        FileListManagerActivity.intoActivity(
                            this@MediaStoreImagesActivity,
                            imageList
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    @SuppressLint("Recycle")
    private fun queryImages(): ArrayList<Uri> {
        val images = ArrayList<Uri>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )
        val selection = MediaStore.Images.Media.DATE_ADDED + " >= ?"
        val selectionArgs = arrayOf(dateToTimestamp(22, 10, 2008).toString() + "")
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        var idColumn = 0
        if (cursor != null) {
            idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        }
        var dateModifiedColumn = 0
        if (cursor != null) {
            dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
        }
        var displayNameColumn = 0
        if (cursor != null) {
            displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        }
        if (cursor == null) {
            return images
        }
        while (cursor.moveToNext()) {
            // Here we'll use the column indexs that we found above.
            val id = cursor.getLong(idColumn)
//            val dateModified = Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
//            val displayName = cursor.getString(displayNameColumn)
            val contentUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//            val lat = 0.0
//            val lon = 0.0
//            val image = MediaStoreImage(id, displayName, dateModified, contentUri, lat, lon)
            images.add(contentUri)
        }
        cursor.close()
        return images
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val time: Long = try {
            simpleDateFormat.parse("$day.$month.$year")!!.time
        } catch (e: Exception) {
            0L
        }
        return TimeUnit.MICROSECONDS.toSeconds(time)
    }

}