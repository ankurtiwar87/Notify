package com.example.notify.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import com.example.notify.Room.FacultyEntity
import com.example.notify.Room.NoticeEntity
import com.example.notify.models.FacultyModel

import com.example.notify.models.NoticeModel
import com.example.notify.repository.NoticeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel(private val noticeRepository: NoticeRepository) :ViewModel() {


  suspend fun fetchNotices(collection: String):List<NoticeModel>{
            return withContext(Dispatchers.IO){
                noticeRepository.getNotices(collection)
            }
    }

    suspend fun getOfflineNotices(collection: String):List<NoticeEntity>{
        return withContext(Dispatchers.IO){
            noticeRepository.getOfflineNotices(collection)
        }
    }
    suspend fun fetchFaculty(collection: String):List<FacultyModel>{
        return withContext(Dispatchers.IO){
            noticeRepository.getFaculty(collection)
        }
    }
    suspend fun getOfflineFaculty(collection: String):List<FacultyEntity>{
        return withContext(Dispatchers.IO){
            noticeRepository.getOfflineFaculty(collection)
        }
    }






}


