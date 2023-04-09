
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.Job
import com.example.nekoshigoto.R
import com.example.nekoshigoto.User
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(private val userList : ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position];
        holder.propic.setImageResource(currentItem.image)
        holder.name.text = currentItem.name
        holder.position.text = currentItem.position
        holder.location.text = currentItem.location

        holder.fav.setOnClickListener{

            if(it.getTag() == R.drawable.saved){
                holder.fav.setImageResource(R.drawable.favorite);
                holder.fav.setTag(R.drawable.favorite);
            }
            else{
                holder.fav.setImageResource(R.drawable.saved);
                holder.fav.setTag(R.drawable.saved);
            }
        };
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val name : TextView = itemView.findViewById(R.id.user_name)
        val position : TextView = itemView.findViewById(R.id.position)
        val location : TextView = itemView.findViewById(R.id.location)
        val fav : ImageButton = itemView.findViewById(R.id.fav)
    }
}