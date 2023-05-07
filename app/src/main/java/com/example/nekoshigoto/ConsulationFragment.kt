package com.example.nekoshigoto


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
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
    private lateinit var navigator : NavController
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var chatList : ArrayList<Chat>
    private lateinit var database: DatabaseReference


    private val messageAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        navigator = findNavController()
        setHasOptionsMenu(true)
        val viewModel : JobSeekerViewModel = ViewModelProvider(requireActivity()).get(JobSeekerViewModel::class.java)
        val jobSeeker : JobSeeker = viewModel.getJobSeeker()
        val seekername = String.format("%s %s",jobSeeker.lname,jobSeeker.fname)
        val username : String = seekername

        database = Firebase.database.getReference("chat").child(String.format("%s",seekername))

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
            val receiver_id = "consultant"


            val sender_id = seekername
            val chat = Chat(sender_id,receiver_id,content,Date(),"unseen",viewModel.getJobSeeker().email)
            database.child(String.format("%s%s%s",Date().toString().subSequence(4,Date().toString().length),sender_id,receiver_id)).setValue(chat)
            database.child("image").setValue(viewModel.getJobSeeker().profilePic.toString())
            chatMessage.text.clear()

        }


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                navigator.popBackStack(R.id.consultantConsultationFragment,true)
                for (childSnapshot: DataSnapshot in dataSnapshot.children) {
                    if (childSnapshot.key.toString() != "image") {
                        val chat = childSnapshot.getValue(Chat::class.java)
                        if (chat != null) {
                            if (!chatList.contains(chat)) {
                                if (chat.receiver == seekername) {

                                    chatList.add(chat)
                                    messageAdapter.add(
                                        ConsultantConsultationFragment.ReceiveMessageItem(
                                            chatList.get(chatList.size - 1)
                                        )
                                    )
                                    newRecyclerView.scrollToPosition(chatList.size - 1);
                                } else if (chat.sender == seekername) {
                                    var duplicate = false
                                    for(mychat in chatList){
                                        if(mychat.date == chat.date){
                                            duplicate = true
                                        }
                                    }
                                    if(!duplicate){
                                        chatList.add(chat)
                                        messageAdapter.add(
                                            ConsultantConsultationFragment.SendMessageItem(
                                                chatList.get(chatList.size - 1)
                                            )
                                        )
                                        newRecyclerView.scrollToPosition(chatList.size - 1);
                                    }




                                }


                            }

                            // ...

                        }
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
            viewHolder.senddate.text = chat.date.toString().subSequence(0,16)
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
            viewHolder.receiverdate.text = chat.date.toString().subSequence(0,16)
        }

    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
        activity?.chgTitle("My Consultation")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePasswordFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
            R.id.logout -> {
                startActivity(Intent(requireContext(), Logout::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}