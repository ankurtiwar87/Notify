package com.example.notify.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notices")
data class NoticeEntity(

    @PrimaryKey(autoGenerate = true)
    val roomId:Int=0,
    val id :String ? = null,
    val title:String?=null,
    val desc:String?=null,
    val imgUrl:String?=null,
    val docUrl:String?=null,
    val date:String?=null,
    val time:String?=null,
    val collectionName:String?=null
)
