package com.embark.notes.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.embark.notes.util.Constants

class Migration1to2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        val TEMP_TABLE_NAME = "NoteAddIndex"

        db.execSQL(
            "CREATE TABLE `$TEMP_TABLE_NAME` (`index` INTEGER, " +
                    "`title` TEXT NOT NULL, " +
                    "`content` TEXT NOT NULL, " +
                    "`isPinned` BIT NOT NULL, " +
                    "PRIMARY KEY(`index`))"
        )

        db.execSQL(
            "INSERT INTO $TEMP_TABLE_NAME(title," +
                    " content," +
                    " isPinned)" +
                    " SELECT title, content, isPinned FROM ${Constants.DB_NAME}"
        )


        db.execSQL("DROP TABLE ${Constants.DB_NAME}")
        db.execSQL("ALTER TABLE $TEMP_TABLE_NAME RENAME TO ${Constants.DB_NAME}")
    }

}

class Migration2to3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ${Constants.DB_NAME} ADD COLUMN lastModified INTEGER")

    }

}