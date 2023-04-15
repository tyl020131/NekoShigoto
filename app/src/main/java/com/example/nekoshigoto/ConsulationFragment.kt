package com.example.nekoshigoto


import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.ReceiverChatBinding
import com.example.nekoshigoto.databinding.SenderChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ConsulationFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var chatList : ArrayList<Chat>
    private lateinit var database: DatabaseReference


    private val messageAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        var sh : SharedPreferences = requireActivity().getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        val userid : String = sh.getString("userid","").toString()
        database = Firebase.database.getReference("chat")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consulation, container, false)

        newRecyclerView = view.findViewById(R.id.chat_content)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        chatList = arrayListOf<Chat>()
        loadData()


        newRecyclerView.adapter = messageAdapter
        newRecyclerView.scrollToPosition(chatList.size-1);


        var sendBtn : ImageButton = view.findViewById(R.id.send_btn)
        var chatMessage : EditText = view.findViewById(R.id.chat_message)

        sendBtn.setOnClickListener {
            var content = chatMessage.text.toString()
            val receiver: TextView = view.findViewById(R.id.chat_receipient)
            var receiver_id = receiver.tag.toString()
            if(userid!="tyl99"){
                receiver_id = "tyl99"
            }
            else{
                receiver_id = "consultant"
            }

            val sender_id = userid
            val chat = Chat(sender_id,receiver_id,content,Date())
            database.child(String.format("%s%s%s",Date(),sender_id,receiver_id)).setValue(chat)
            chatMessage.text.clear()

        }


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(childSnapshot: DataSnapshot in dataSnapshot.children){
                    val chat = childSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        if(!chatList.contains(chat)){
                            if(chat.receiver == userid){
                                chatList.add(chat)
                                messageAdapter.add(ReceiveMessageItem(chatList.get(chatList.size-1)))
                                newRecyclerView.scrollToPosition(chatList.size-1);
                            }
                            else{
                                chatList.add(chat)
                                messageAdapter.add(SendMessageItem(chatList.get(chatList.size-1)))
                                newRecyclerView.scrollToPosition(chatList.size-1);
                            }


                        }
                        
                        // ...

                    }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
        return view;


    }
    private fun loadData(){







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