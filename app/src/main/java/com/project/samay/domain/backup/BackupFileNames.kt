package com.project.samay.domain.backup

enum class BackupFileNames(val fileName: String) {
    DOMAIN_DATABASE("domain_backup"),
    TASKS_DATABASE("tasks_backup")
}