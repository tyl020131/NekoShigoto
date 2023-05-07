package com.example.nekoshigoto

import ChatRoomAdapter
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class ChatRoomFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var rooms : ArrayList<ChatRoom>
    private lateinit var newRecyclerView: RecyclerView
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var crAdapter : ChatRoomAdapter
    private lateinit var navigator : NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        navigator = findNavController()


        rooms = ArrayList<ChatRoom>()
        crAdapter = ChatRoomAdapter(rooms,navigator)
        database = Firebase.database.getReference("chat")
        val view = inflater.inflate(R.layout.fragment_chat_room, container, false)

        newRecyclerView = view.findViewById(R.id.chatrooms)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        newRecyclerView.adapter = crAdapter

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(chatroom: DataSnapshot in dataSnapshot.children){
                    var chatRoom = ChatRoom()
                    var email = ""
                    var content = ""
                    var status = "unseen"
                    var image = ""
                    var contentid = ""
                    for(chat in chatroom.children){
                        Log.d(TAG,chat.toString())
                        if(chat.key!="image") {
                            val mychat = chat.getValue(Chat::class.java)
                            if (mychat != null) {
                                content = mychat.content.toString()
                                status = mychat.status.toString()
                                email = mychat.email.toString()
                                contentid = chat.key.toString()

                            }
                        }
                        else{
                            val image = chat.getValue(String::class.java)
                            chatRoom.image = image.toString()



                        }

                    }

                    chatRoom.user = chatroom.key.toString()
                    chatRoom.lastMessage = content
                    chatRoom.email = email
                    chatRoom.status = status
                    chatRoom.contentid = contentid

                    var exist = false
                    for(room in rooms){
                        if(room.email==chatRoom.email){
                            rooms[rooms.indexOf(room)] = chatRoom
                            exist = true
                            break

                        }
                    }
                    if(!exist){
                        rooms.add(chatRoom)
                    }

                }
                rooms.sortByDescending { room -> room.contentid }
                crAdapter = ChatRoomAdapter(rooms,navigator)
                newRecyclerView.adapter = crAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }


        Log.d(ContentValues.TAG,rooms.toString())
        database.addValueEventListener(postListener)
        return view;

    }





}