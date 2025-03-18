package cc.fastcv.file_manager

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import cc.fastcv.file_manager.dic.DocumentManagerActivity
import cc.fastcv.file_manager.dic.FileManagerActivity
import java.io.File

object FileManager {

    @Throws(IllegalArgumentException::class)
    fun openDicByPath(context: Context, path: String) {
        if (path.startsWith("content:") || path.startsWith("file:")) {
            //是uri路径
            val uri = Uri.parse(path)
            if (!isDirectoryFromUri(context, uri)) {
                throw IllegalArgumentException("此uri不是目录uri")
            } else {
                if ("file".equals(uri.scheme, ignoreCase = true)) {
                    FileManagerActivity.intoActivity(context, uri.path!!)
                } else {
                    DocumentManagerActivity.intoActivity(context, uri)
                }
            }
        } else {
            //默认文件夹一定存在
            val root = File(path)
            if (!root.exists() || !root.isDirectory()) {
                throw IllegalArgumentException("此File path不存在或者不是目录")
            } else {
                FileManagerActivity.intoActivity(context, root.absolutePath)

            }
        }
    }

    @Throws(IllegalArgumentException::class)
    fun openDicByUri(context: Context, uri: Uri) {
        if (!isDirectoryFromUri(context, uri)) {
            throw IllegalArgumentException("此uri不是目录uri")
        } else {
            if ("file".equals(uri.scheme, ignoreCase = true)) {
                FileManagerActivity.intoActivity(context, uri.path!!)
            } else {
                DocumentManagerActivity.intoActivity(context, uri)
            }
        }
    }

    private fun isDirectoryFromUri(context: Context, uri: Uri): Boolean {
        return if ("file".equals(uri.scheme, ignoreCase = true)) {
            isFileDirectory(uri)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            isDocumentDirectory(context, uri)
        } else {
            false
        }
    }

    private fun isFileDirectory(uri: Uri): Boolean {
        if (uri.path == null) {
            return false
        }

        try {
            val file = File(uri.path!!)
            return file.isDirectory
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun isDocumentDirectory(context: Context, uri: Uri): Boolean {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        if (documentFile == null) {
            return false
        } else {
            return documentFile.isDirectory
        }

    }


}