package com.example.nekoshigoto

import java.util.Date

data class Chat(val sender : String?=null, val receiver : String?=null, val content : String?=null, val date: Date?=null,var status : String = "unseen", val email : String = "")
