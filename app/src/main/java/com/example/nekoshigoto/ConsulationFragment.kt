package com.example.nekoshigoto


import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.ReceiverChatBinding
import com.example.nekoshigoto.databinding.SenderChatBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*


class ConsulationFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var chatList : ArrayList<Chat>

    private val messageAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consulation, container, false)

        newRecyclerView = view.findViewById(R.id.chat_content)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        chatList = arrayListOf<Chat>()
        loadData()

        chatList.forEach {
            if(it.sender == 1){
                messageAdapter.add(SendMessageItem(it))
            }
            else{
                messageAdapter.add(ReceiveMessageItem(it))
            }
        }
        newRecyclerView.adapter = messageAdapter
        newRecyclerView.scrollToPosition(chatList.size-1);

        var sendBtn : ImageButton = view.findViewById(R.id.send_btn)
        var chatMessage : EditText = view.findViewById(R.id.chat_message)

        sendBtn.setOnClickListener {
            var content = chatMessage.text.toString()
            chatList.add(Chat(1,2,content, Date("3 March 2023")) )
            messageAdapter.add(SendMessageItem(chatList.get(chatList.size-1)))
            newRecyclerView.scrollToPosition(chatList.size-1);
            chatMessage.text.clear()

        }

        return view;


    }



    private fun loadData(){
        chatList.add(Chat(1,2,"Hi", Date("3 March 2023")) )
        chatList.add(Chat(1,2,"Wassup Bro", Date("3 March 2023")) )
        chatList.add(Chat(2,1,"Oi", Date("3 March 2023")) )
        chatList.add(Chat(2,1,"Ah Pong Xiao le", Date("3 March 2023")) )
        chatList.add(Chat(1,2,"KC Xiaole", Date("3 March 2023")) )
        chatList.add(Chat(1,2,"Hhehehehehehehe", Date("3 March 2023")) )
        chatList.add(Chat(1,2,"noob", Date("3 March 2023")) )
        chatList.add(Chat(2,1,"nickee xiaole", Date("3 March 2023")) )
        chatList.add(Chat(2,1,"diam la ah pong", Date("3 March 2023")) )
        chatList.add(Chat(1,2,"weak chicken", Date("3 March 2023")) )
        chatList.add(Chat(2,1,"weak chinken", Date("3 March 2023")) )






    }

    class SendMessageItem(private val chat: Chat) : BindableItem<SenderChatBinding>() {

        override fun getLayout(): Int {
            return R.layout.sender_chat
        }

        override fun initializeViewBinding(view: View): SenderChatBinding {
            return SenderChatBinding.bind(view)
        }


        override fun bind(viewHolder: SenderChatBinding, position: Int) {
            viewHolder.message = chat
        }

    }

    class ReceiveMessageItem(private val chat: Chat) : BindableItem<ReceiverChatBinding>() {

        override fun getLayout(): Int {
            return R.layout.receiver_chat
        }

        override fun initializeViewBinding(view: View): ReceiverChatBinding {
            return ReceiverChatBinding.bind(view)
        }

        override fun bind(viewHolder: ReceiverChatBinding, position: Int) {
            viewHolder.message = chat
        }

    }


}