
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.Job
import com.example.nekoshigoto.JobSeekerViewModel
import com.example.nekoshigoto.R
import com.example.nekoshigoto.Vacancy
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.FirebaseFirestore

class SavedAdapter(private val jobList : ArrayList<Vacancy>, private val viewModel: JobSeekerViewModel) :
    RecyclerView.Adapter<SavedAdapter.MyViewHolder>() {
    private lateinit var itemView :View
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position];
        val imgUri = currentItem.image.toUri().buildUpon().scheme("https").build()
        holder.propic.load(imgUri)
        holder.company.text = currentItem.companyName
        holder.vacancy.text = currentItem.position
        holder.location.text = currentItem.location
        holder.mode.text = currentItem.mode

        holder.fav.setImageResource(R.drawable.favorite);
        holder.fav.setOnClickListener {
            jobList.removeAt(position)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, jobList.size)
            holder.itemView.setVisibility(View.GONE)
            val jobseeker = viewModel.getJobSeeker()
            val saveid = String.format("%s%s",jobseeker.email,currentItem.vacancyid)
            db.collection("Job Seeker").document(jobseeker.email).collection("saves").document(saveid).delete()
            Toast.makeText(itemView.context, "Job Removed from Saved Successfully", Toast.LENGTH_SHORT).show()

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