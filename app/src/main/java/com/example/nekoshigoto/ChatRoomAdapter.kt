
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.*
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.FirebaseFirestore

class ChatRoomAdapter(private val rooms : ArrayList<ChatRoom>, private val navigator : NavController) :
    RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder>() {


    private lateinit var itemView :View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_room,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = rooms[position];
        val imgUri = currentItem.image.toUri().buildUpon().scheme("https").build()
        holder.propic.load(imgUri)
        holder.username.text = currentItem.user
        holder.content.text = currentItem.lastMessage
        if(currentItem.status == "unseen"){
            holder.content.setTextColor(holder.content.resources.getColor(R.color.white))
            holder.content.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f)
            holder.cont.setBackgroundColor(holder.cont.resources.getColor(R.color.color4))
        }
        holder.cont.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("chatroomid",currentItem.user)
            bundle.putString("chatroomemail",currentItem.email)
            bundle.putString("chatroomimage",currentItem.image)
            navigator.navigate(R.id.action_chatRoomFragment_to_consultantConsultationFragment,bundle)
        }
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val cont : ConstraintLayout = itemView.findViewById(R.id.roomm)
        val propic : ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val username : TextView = itemView.findViewById(R.id.username)
        val content : TextView = itemView.findViewById(R.id.content)

    }
}