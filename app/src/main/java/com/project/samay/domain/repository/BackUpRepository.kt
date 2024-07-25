import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class BackUpRepository {
    fun backupDatabase(context: Context) {
        val resolver = context.contentResolver
        val dbFile = File(context.getDatabasePath("samay_database").absolutePath)

        if (!dbFile.exists()) {
            Toast.makeText(context, "Database file not found", Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 (API level 29) and above
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "samay_database_backup.db")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/x-sqlite3")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/DatabaseBackups")
            }

            val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), values)

            uri?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    FileInputStream(dbFile).use { inputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            outputStream?.write(buffer, 0, length)
                        }
                    }
                }
                Toast.makeText(context, "Backup successful", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Backup failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            // For devices running Android 12 and below
            val backupDir = File(context.getExternalFilesDir(null), "Documents/DatabaseBackups")
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }
            val backupFile = File(backupDir, "samay_database_backup.db")

            // Always create a new backup file, overwriting if it exists
            FileInputStream(dbFile).use { input ->
                FileOutputStream(backupFile, false).use { output ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        output.write(buffer, 0, length)
                    }
                }
            }
            Toast.makeText(context, "Backup successful", Toast.LENGTH_SHORT).show()
        }
    }

    fun restoreDatabase(context: Context) {
        val resolver = context.contentResolver
        val dbFile = File(context.getDatabasePath("samay_database").absolutePath)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val projection = arrayOf(MediaStore.MediaColumns._ID)
            val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf("samay_database_backup.db")
            val cursor = resolver.query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val uri = MediaStore.Files.getContentUri("external").buildUpon().appendPath(id.toString()).build()

                    resolver.openInputStream(uri)?.use { inputStream ->
                        FileOutputStream(dbFile).use { outputStream ->
                            val buffer = ByteArray(1024)
                            var length: Int
                            while (inputStream.read(buffer).also { length = it } > 0) {
                                outputStream.write(buffer, 0, length)
                            }
                        }
                        Toast.makeText(context, "Restore successful", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(context, "Restore failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No backup found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Fallback for devices running Android 12 and below
            val backupFile = File(context.getExternalFilesDir(null), "Documents/DatabaseBackups/samay_database_backup.db")
            if (backupFile.exists()) {
                FileInputStream(backupFile).use { input ->
                    FileOutputStream(dbFile).use { output ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (input.read(buffer).also { length = it } > 0) {
                            output.write(buffer, 0, length)
                        }
                    }
                }
                Toast.makeText(context, "Restore successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Backup file not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
