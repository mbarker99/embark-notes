package com.embark.notes.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.embark.notes.util.Constants

class Migration1to2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        val TEMP_TABLE_NAME = "NoteAddIndex"

        db.execSQL(
            "CREATE TABLE `$TEMP_TABLE_NAME` (`${Constants.DB_INDEX}` INTEGER, " +
                    "`${Constants.DB_TITLE}` TEXT NOT NULL, " +
                    "`${Constants.DB_CONTENT}` TEXT NOT NULL, " +
                    "`${Constants.DB_IS_PINNED}` BIT NOT NULL, " +
                    "PRIMARY KEY(`${Constants.DB_INDEX}`))"
        )

        db.execSQL(
            "INSERT INTO $TEMP_TABLE_NAME(${Constants.DB_TITLE}," +
                    " ${Constants.DB_CONTENT}," +
                    " ${Constants.DB_IS_PINNED})" +
                    " SELECT ${Constants.DB_TITLE}, ${Constants.DB_CONTENT}, ${Constants.DB_IS_PINNED} FROM ${Constants.DB_TABLE_NAME}"
        )

        db.execSQL("DROP TABLE ${Constants.DB_TABLE_NAME}")
        db.execSQL("ALTER TABLE $TEMP_TABLE_NAME RENAME TO ${Constants.DB_TABLE_NAME}")
    }
}

class Migration2to3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ${Constants.DB_TABLE_NAME} ADD COLUMN ${Constants.DB_LAST_MODIFIED} INTEGER")

    }
}

class Migration3to4 : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ${Constants.DB_TABLE_NAME} ADD COLUMN ${Constants.DB_CREATED_AT} INTEGER")
    }
}