package com.example.studybuddyai.chatgptfiles.response

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)