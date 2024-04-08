package com.example.notify.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faculty")
data class FacultyEntity(

    @PrimaryKey(autoGenerate = true)
    val roomId:Int=0,
    val id :String ? = null,
    val name:String?=null,
    val phoneNo:String?=null,
    val cabinNo:String?=null,
    val subject:String?=null,
    val email:String?=null,
    val imageUrl:String?=null,
    val year:String?=null,
    val block:String?=null
)
