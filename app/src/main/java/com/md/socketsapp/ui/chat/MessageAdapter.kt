package com.md.socketsapp.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.md.socketsapp.R
import com.md.socketsapp.models.Message
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.MessagesViewHolder>() {

    var messagesList: List<Message> = arrayListOf()

    override fun getItemCount(): Int =
        messagesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessagesViewHolder(v)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) =
        holder.bindViews(messagesList[position])

    inner class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindViews(message: Message) {
            itemView.name.text = message.user
            itemView.text.text = message.message
        }
    }
}