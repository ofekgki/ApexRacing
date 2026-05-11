package com.example.apexracing.models

import com.google.firebase.Timestamp

class Post (
    var postId: String,
    var userId: String,
    var userName: String,
    var likes: Int,
    var time: Timestamp,
    var postText: String,
){

}