
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.Application
import com.example.nekoshigoto.R
import com.google.android.material.imageview.ShapeableImageView

class ApplicationAdapter(private val applicationList : ArrayList<Application>) :
    RecyclerView.Adapter<ApplicationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.application_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = applicationList[position];
        val imgUri = currentItem.image.toUri().buildUpon().scheme("https").build()
        holder.propic.load(imgUri)
        holder.company.text = currentItem.company
        holder.vacancy.text = currentItem.vacancy
        holder.mode.text = currentItem.status
        if(currentItem.status == "Rejected")
            holder.mode.setTextColor(ContextCompat.getColor(holder.mode.context, R.color.color8))

        holder.cont.setOnClickListener {
            val bundle = bundleOf("jobname" to currentItem.id)
            it.findNavController().navigate(R.id.action_activityFragment_to_jobDetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val mode : TextView = itemView.findViewById(R.id.mode)
        val cont : ConstraintLayout = itemView.findViewById(R.id.jobb)
    }
}