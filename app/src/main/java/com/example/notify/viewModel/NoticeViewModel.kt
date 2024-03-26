package com.example.notify.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify.models.NoticeModel
import com.example.notify.repository.NoticeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoticeViewModel:ViewModel() {
    private val _notices = MutableLiveData<List<NoticeModel>>()
    val notices: LiveData<List<NoticeModel>> = _notices

    private val noticeRepository = NoticeRepository("collection")

    init {
        fetchNotices()
    }

    private fun fetchNotices() {
        viewModelScope.launch(Dispatchers.IO) {
            val noticesList = noticeRepository.getNotices()
            _notices.postValue(noticesList)
        }
    }
}