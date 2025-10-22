package com.example.testapplication.ui.websocketchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemMessageServerBinding
import com.example.testapplication.databinding.ItemMessageUserBinding
import com.example.testapplication.model.Message
import com.example.testapplication.utils.TimeHelper.formatTimeHHMM

class MessageAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeUser = 1
    private val typeSever = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) typeUser else typeSever
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == typeUser) {
            val binding = ItemMessageUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            UserViewHolder(binding)
        } else {
            val binding = ItemMessageServerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ServerViewHolder(binding)
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) holder.bind(message)
        else if (holder is ServerViewHolder) holder.bind(message)
    }

    inner class UserViewHolder(private val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.text
            binding.tvTime.text = formatTimeHHMM(message.timestamp)
        }
    }

    inner class ServerViewHolder(private val binding: ItemMessageServerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.text
            binding.tvTime.text = formatTimeHHMM(message.timestamp)
        }
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
