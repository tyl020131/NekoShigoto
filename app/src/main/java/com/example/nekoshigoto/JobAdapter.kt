
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.*
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.FirebaseFirestore

class JobAdapter(private val jobList : ArrayList<Vacancy>,private val email:String,private val mySaved : ArrayList<Save>) :
    RecyclerView.Adapter<JobAdapter.MyViewHolder>() {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var itemView :View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_list,parent,false)
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
        holder.fav.setTag(R.drawable.saved)
        if(mySaved.contains(Save(currentItem.vacancyid))){
            holder.fav.setImageResource(R.drawable.favorite);
        }
        else{
            holder.fav.setImageResource(R.drawable.saved);
        }


        holder.fav.setOnClickListener{
            val vacancyname = String.format("%s%s",holder.vacancy,holder.company).lowercase()
            val saveid = String.format("%s%s",email,currentItem.vacancyid)

            if(it.getTag() == R.drawable.saved){

                holder.fav.setImageResource(R.drawable.favorite);
                holder.fav.setTag(R.drawable.favorite);
                db.collection("Job Seeker").document(email).collection("saves").document(saveid).set(Save(currentItem.vacancyid))
                Toast.makeText(itemView.context, "Job Saved Successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                holder.fav.setImageResource(R.drawable.saved);
                holder.fav.setTag(R.drawable.saved);
                db.collection("Job Seeker").document(email).collection("saves").document(saveid).delete()
                Toast.makeText(itemView.context, "Job Removed from Saved Successfully", Toast.LENGTH_SHORT).show()
            }
        };
        holder.jobb.setOnClickListener{
            val bundle = bundleOf("dataKey" to currentItem.vacancyid)
            it.findNavController().navigate(R.id.action_homeFragment_to_jobDetailsUser, bundle)
        }
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val location : TextView = itemView.findViewById(R.id.location)
        val mode : TextView = itemView.findViewById(R.id.mode)
        val fav : ImageButton = itemView.findViewById(R.id.fav)
        val jobb : androidx.constraintlayout.widget.ConstraintLayout = itemView.findViewById(R.id.jobb)
    }
}