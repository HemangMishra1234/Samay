package com.project.samay.domain.backup

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.project.samay.data.source.local.DatabaseNames
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class BackupRepo {

    fun backupAndShareDatabase(context: Context) {
        val dbNames = DatabaseNames.entries.map {
            it.dbName
        }
        val backupDir = File(context.getExternalFilesDir(null), "Database backups")

        val backupFiles = backupAllDatabases(context, dbNames, backupDir)

        val zipFile = File(backupDir, "backup_databases.zip")
        zipBackupFiles(backupFiles, zipFile)

        shareZipFile(context, zipFile)
    }

    private fun backupAllDatabases(
        context: Context,
        databaseNames: List<String>,
        backupDir: File
    ): List<File> {
        val backupFiles = mutableListOf<File>()

        // Create backup directory if it doesn't exist
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }

        databaseNames.forEach { dbName ->
            val databaseFile = File(context.getDatabasePath(dbName).absolutePath)
            val backupFile = File(backupDir, "$dbName-backup.db")

            try {
                FileInputStream(databaseFile).use { input ->
                    FileOutputStream(backupFile).use { output ->
                        input.copyTo(output)
                    }
                }
                backupFiles.add(backupFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return backupFiles
    }

    private fun zipBackupFiles(backupFiles: List<File>, zipFile: File) {
        try {
            ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
                backupFiles.forEach { file ->
                    FileInputStream(file).use { input ->
                        val entry = ZipEntry(file.name)
                        zipOut.putNextEntry(entry)
                        input.copyTo(zipOut)
                    }
                }
            }
            println("Successfully created the zip")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareZipFile(context: Context, zipFile: File) {
        val uri: Uri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", zipFile)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, zipFile)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "share backup"))

    }

}