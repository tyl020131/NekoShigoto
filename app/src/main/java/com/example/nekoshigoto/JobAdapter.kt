
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.Job
import com.example.nekoshigoto.R
import com.example.nekoshigoto.Vacancy
import com.google.android.material.imageview.ShapeableImageView

class JobAdapter(private val jobList : ArrayList<Vacancy>) :
    RecyclerView.Adapter<JobAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position];
        val imgUri = currentItem.image.toUri().buildUpon().scheme("https").build()
        holder.propic.load(imgUri)
        holder.company.text = currentItem.companyName
        holder.vacancy.text = currentItem.position
        holder.location.text = "Penang,Malaysia"
        holder.mode.text = currentItem.mode

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
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val location : TextView = itemView.findViewById(R.id.location)
        val mode : TextView = itemView.findViewById(R.id.mode)
        val fav : ImageButton = itemView.findViewById(R.id.fav)
    }
}