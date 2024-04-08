package com.example.notify.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notify.models.NoticeModel

@Database(entities = [NoticeEntity::class,FacultyEntity::class], version = 1, exportSchema = false)
abstract class NoticeDatabase :RoomDatabase() {

    abstract fun getNoticeDao(): NoticeDao

    companion object {
        @Volatile
        private var INSTANCE: NoticeDatabase? = null

        fun getDatabase(context: Context): NoticeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoticeDatabase::class.java,
                    "NotifyDB.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}