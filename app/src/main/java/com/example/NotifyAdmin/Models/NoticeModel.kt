package com.ankur.admin_notifycampus.Models

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
