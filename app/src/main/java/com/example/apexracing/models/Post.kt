package com.example.apexracing.models

class Post(
    var postId: String = "",
    var userId: String = "",
    var userName: String = "",
    var likes: Int = 0,
    var time: Long = 0L,
    var postText: String = "",
    var likedBy: Map<String, Boolean> = emptyMap()

) {

}