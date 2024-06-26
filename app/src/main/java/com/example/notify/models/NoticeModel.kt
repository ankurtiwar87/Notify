package com.example.notify.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NoticeModel(
    val id :String ? = null,
    val title:String?=null,
    val desc:String?=null,
    val imgUrl:String?=null,
    val docUrl:String?=null,
    val date:String?=null,
    val time:String?=null,
    val collectionName:String?=null
)
