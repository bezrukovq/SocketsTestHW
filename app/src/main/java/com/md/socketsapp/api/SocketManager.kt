package com.md.socketsapp.api

import android.content.Context
import com.md.socketsapp.utils.Const
import okhttp3.*

class SocketManager(
    private val context: Context,
    private var messageCallback: (String?) -> Unit,
    private var getMessagesCallback: (String?) -> Unit
) {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private lateinit var socket: WebSocket

    fun initSocketManager() {
        val request: Request = Request.Builder().url(Const.API_CHAT).build()
        val listener = object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                val deviceId =
                    context
                        .getSharedPreferences(Const.PREFERENCES, Context.MODE_PRIVATE)
                        .getString(Const.DEVICE_ID, "")
                val json = "{ \"device_id\":\"$deviceId\" }"
                webSocket.send(json)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                if (text == "{\"status\": \"ok\"}") {
                    messageCallback(text)
                } else if (text.contains("items")) {
                    getMessagesCallback(text)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                socket = client.newWebSocket(request, this)
                messageCallback(null)
            }

        }
        socket = client.newWebSocket(request, listener)
    }

    fun getMessages(count: Int) {
        val json = "{ \"history\": { \"limit\": $count} }"
        socket.send(json)
    }

    fun sendMessage(message: String) {
        val json = "{ \"message\":\"$message\" }"
        socket.send(json)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
