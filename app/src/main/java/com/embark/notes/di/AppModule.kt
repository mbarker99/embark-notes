package com.embark.notes.di

import android.content.Context
import androidx.room.Room
import com.embark.notes.db.NoteDao
import com.embark.notes.db.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context) : NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "NoteDatabase"
        ).fallbackToDestructiveMigration()
            .addMigrations(NoteDatabase.MIGRATION_1_2, NoteDatabase.MIGRATION_2_3)
            .build()

}