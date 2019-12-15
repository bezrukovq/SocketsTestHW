package com.md.socketsapp.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.md.socketsapp.R
import com.md.socketsapp.api.SocketManager
import com.md.socketsapp.models.GetMessagesResponse
import com.md.socketsapp.models.Message
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val adapter = MessageAdapter()

    private val socketManager: SocketManager = SocketManager(this, { message ->
        runOnUiThread {
            if (message != null) {
                getMessages()
            }
        }

    }, { messages ->
        runOnUiThread {
            if (messages != null) {
                val response: GetMessagesResponse =
                    Gson().fromJson(messages, GetMessagesResponse::class.java)
                response.items?.let { showMessages(it) }
            }
        }
    })

    private fun showMessages(messages: List<Message>) {
        adapter.messagesList = messages
        adapter.notifyDataSetChanged()
        et_message.setText("")
    }

    private fun getMessages() = socketManager.getMessages(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        socketManager.initSocketManager()
        rv_messages.adapter = adapter
        btn_send_message.setOnClickListener {
            socketManager.sendMessage(et_message.text.toString())
        }
    }
}
