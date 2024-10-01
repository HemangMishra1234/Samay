package com.project.samay.domain.backup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.project.samay.data.source.local.DatabaseNames
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class BackupRepo {

    fun backupAndShareDatabase(context: Context) {
        val dbNames = DatabaseNames.entries.map { it.dbName }
        val backupDir = File(context.getExternalFilesDir(null), "Database backups")

        val backupFiles = backupAllDatabases(context, dbNames, backupDir)

        val zipFile = File(backupDir, "backup_databases.zip")
        zipBackupFiles(backupFiles, zipFile)

        if (zipFile.exists() && zipFile.length() > 0) {
            shareZipFile(context, zipFile)
        } else {
            Log.i("Backup Repo", "Zip file is empty or does not exist.")
        }
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

            Log.i("Backup Repo", "Database file path: ${databaseFile.absolutePath}")
            Log.i("Backup Repo", "Backup file path: ${backupFile.absolutePath}")

            if (databaseFile.exists()) {
                try {
                    FileInputStream(databaseFile).use { input ->
                        FileOutputStream(backupFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    backupFiles.add(backupFile)
                } catch (e: IOException) {
                    Log.e("Backup Repo", "Error copying database file: $e")
                }
            } else {
                Log.e("Backup Repo", "Database file does not exist: ${databaseFile.absolutePath}")
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
            Log.i("Backup Repo", "Successfully created the zip")
        } catch (e: Exception) {
            Log.e("Backup Repo", "Error zipping backup files: $e")
        }
    }

    private fun shareZipFile(context: Context, zipFile: File) {
        val uri: Uri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", zipFile)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share backup"))
    }

    fun restoreDatabase(context: Context, zipFile: File) {
        val backupDir = File(context.getExternalFilesDir(null), "Database backups")
        unzipBackupFiles(zipFile, backupDir)
        Log.i("Backup Remo", "Presently I am in the restore function")

        val dbNames = DatabaseNames.entries.map { it.dbName }
        dbNames.forEach { dbName ->
            val backupFile = File(backupDir, "$dbName-backup.db")
            val databaseFile = File(context.getDatabasePath(dbName).absolutePath)

            if (backupFile.exists()) {
                try {
                    FileInputStream(backupFile).use { input ->
                        FileOutputStream(databaseFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.i("Backup Repo", "Restored database: $dbName")
                } catch (e: IOException) {
                    Log.e("Backup Repo", "Error restoring database: $e")
                }
            } else {
                Log.e("Backup Repo", "Backup file does not exist: ${backupFile.absolutePath}")
            }
        }
    }

    private fun unzipBackupFiles(zipFile: File, targetDir: File) {
        try {
            ZipInputStream(FileInputStream(zipFile)).use { zipIn ->
                var entry: ZipEntry? = zipIn.nextEntry
                while (entry != null) {
                    val filePath = File(targetDir, entry.name).absolutePath
                    if (!entry.isDirectory) {
                        FileOutputStream(filePath).use { output ->
                            zipIn.copyTo(output)
                        }
                    } else {
                        File(filePath).mkdirs()
                    }
                    zipIn.closeEntry()
                    entry = zipIn.nextEntry
                }
            }
            Log.i("Backup Repo", "Successfully extracted the zip")
        } catch (e: IOException) {
            Log.e("Backup Repo", "Error extracting zip file: $e")
        }
    }
}