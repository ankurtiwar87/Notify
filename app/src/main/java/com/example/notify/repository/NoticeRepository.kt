package com.example.notify.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.notify.Room.FacultyEntity
import com.example.notify.Room.NoticeDao
import com.example.notify.Room.NoticeEntity
import com.example.notify.models.FacultyModel
import com.example.notify.models.NoticeModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NoticeRepository(private val noticeDao: NoticeDao) {


    suspend fun getNotices(collection: String): List<NoticeModel> {
        val list = ArrayList<NoticeModel>()

        try {
            val documents = Firebase.firestore.collection(collection).get().await()
            val offlineDataList = ArrayList<NoticeEntity>()

            // Convert documents to NoticeModel objects
            documents.forEach { doc ->
                val data = doc.toObject(NoticeModel::class.java)
                list.add(data)

                // Convert NoticeModel to NoticeEntity and add to list
                val offlineData = NoticeEntity(
                    id = data.id,
                    title = data.title,
                    desc = data.desc,
                    imgUrl = data.imgUrl,
                    date = data.date,
                    collectionName = data.collectionName,
                    docUrl = data.docUrl,
                    time = data.time
                )
                // Check if the notice already exists in the database
                val existingNotice = noticeDao.getNoticeById(data.id!!)
                if (existingNotice != null) {
                    // If the notice exists, update it instead of adding a new one
                    noticeDao.updateNotice(offlineData)
                } else {
                    // If the notice doesn't exist, add it to the list for insertion
                    offlineDataList.add(offlineData)
                }
            }


            // Insert all NoticeEntities in a single batch
            withContext(Dispatchers.IO) {
                noticeDao.insertOrUpdateNotices(offlineDataList)
            }

            deleteNoticesNotInFirebase(offlineDataList, collection)

        } catch (e: Exception) {
            Log.e("repo", "Error getting notices: ${e.message}")
        }

        return list
    }



    suspend fun getFaculty(collection: String): List<FacultyModel> {
        val list = ArrayList<FacultyModel>()

        try {
            val documents = Firebase.firestore.collection(collection).get().await()
            val offlineDataList = ArrayList<FacultyEntity>()

            // Convert documents to FacultyModel objects
            documents.forEach { doc ->
                val data = doc.toObject(FacultyModel::class.java)
                list.add(data)

                // Convert FacultyModel to FacultyEntity and add to list
                val offlineData = FacultyEntity(
                    id = data.id,
                    name = data.name,
                    subject = data.subject,
                    cabinNo = data.cabinNo,
                    phoneNo = data.phoneNo,
                    block = data.block,
                    imageUrl = data.imageUrl,
                    year = data.year,
                    email = data.email
                    // Include other properties as needed
                )
                // Add all faculty data to offlineDataList for insertion/update
                offlineDataList.add(offlineData)
            }

            // Insert or update all FacultyEntities in a single batch
            withContext(Dispatchers.IO) {
                noticeDao.insertOrUpdateFaculty(offlineDataList)
            }

            deleteFacultyNotInFirebase(offlineDataList,collection)

        } catch (e: Exception) {
            Log.e("repo", "Error getting faculty: ${e.message}")
        }

        return list
    }

    suspend fun getOfflineNotices(collection: String): List<NoticeEntity> {
        return noticeDao.getAllNotices(collectionName = collection)
    }

    suspend fun getOfflineFaculty(collection: String):List<FacultyEntity>{
        return noticeDao.getAllFaculty(collection)
    }


        private suspend fun deleteNoticesNotInFirebase(
        offlineDataList: List<NoticeEntity>,
        collection: String
    ) {
        val localNoticeIds = offlineDataList.map { it.id }
        val noticesToDelete =
            noticeDao.getAllNotices(collection).filter { it.id !in localNoticeIds }
        withContext(Dispatchers.IO) {
            noticeDao.deleteNotice(noticesToDelete)
        }
    }

    private suspend fun deleteFacultyNotInFirebase(
        offlineDataList: List<FacultyEntity>,
        collection: String
    ) {
        val localFacultyIds = offlineDataList.map { it.id }
        val facultyToDelete =
            noticeDao.getAllFaculty(collection).filter { it.id !in localFacultyIds }
        withContext(Dispatchers.IO) {
            noticeDao.deleteFaculty(facultyToDelete)
        }
    }


}
