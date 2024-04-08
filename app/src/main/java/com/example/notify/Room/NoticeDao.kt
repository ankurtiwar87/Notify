package com.example.notify.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notify.models.NoticeModel

@Dao
interface NoticeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotice(notice: NoticeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNotices(notices: List<NoticeEntity>)
    // Get a notice by its ID
    @Query("SELECT * FROM notices WHERE id = :id")
    suspend fun getNoticeById(id: String): NoticeEntity?

    @Update
    suspend fun updateNotice(notice: NoticeEntity)

    @Delete
    suspend fun deleteNotice(notice: NoticeEntity)
    @Delete
    suspend fun deleteNotice(notice: List<NoticeEntity>)

    @Query("SELECT * FROM notices WHERE collectionName = :collectionName")
    suspend fun getAllNotices(collectionName: String):List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFaculty(faculty: FacultyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFaculty(faculty: List<FacultyEntity>)

    // Get a faculty by its ID
    @Query("SELECT * FROM faculty WHERE id = :id")
    suspend fun getFacultyById(id: String): FacultyEntity?

    @Update
    suspend fun updateFaculty(faculty: FacultyEntity)

    @Delete
    suspend fun deleteFaculty(faculty: FacultyEntity)
    @Delete
    suspend fun deleteFaculty(faculty: List<FacultyEntity>)

    @Query("SELECT * FROM faculty WHERE year = :collectionName")
    suspend fun getAllFaculty(collectionName: String):List<FacultyEntity>

}