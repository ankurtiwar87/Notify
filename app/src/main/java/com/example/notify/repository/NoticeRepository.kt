package com.example.notify.repository

import com.example.notify.models.NoticeModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class NoticeRepository(private val collectionName: String) {

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionRef = firestore.collection(collectionName)

    suspend fun getNotices(): List<NoticeModel> {
        return try {
            val querySnapshot = collectionRef.get().await()
            val noticeList = mutableListOf<NoticeModel>()
            for (document in querySnapshot.documents) {
                val notice = document.toObject(NoticeModel::class.java)
                notice?.let { it ->
                    noticeList.add(it)
                }
            }
            noticeList
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }
}