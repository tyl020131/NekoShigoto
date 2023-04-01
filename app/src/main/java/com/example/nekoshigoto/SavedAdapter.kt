
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.JobViewModel
import com.example.nekoshigoto.R
import com.google.android.material.imageview.ShapeableImageView

class SavedAdapter(private val jobList : ArrayList<JobViewModel>) :
    RecyclerView.Adapter<SavedAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position];
        holder.propic.setImageResource(currentItem.image)
        holder.company.text = currentItem.company
        holder.vacancy.text = currentItem.vacancy
        holder.location.text = currentItem.location
        holder.mode.text = currentItem.mode

        holder.fav.setImageResource(R.drawable.favorite);
        holder.fav.setOnClickListener {
            jobList.removeAt(position)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, jobList.size)
            holder.itemView.setVisibility(View.GONE)

        };
    }
    override fun getItemCount(): Int {
        return jobList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val location : TextView = itemView.findViewById(R.id.location)
        val mode : TextView = itemView.findViewById(R.id.mode)
        val fav : ImageButton = itemView.findViewById(R.id.fav)
    }
}