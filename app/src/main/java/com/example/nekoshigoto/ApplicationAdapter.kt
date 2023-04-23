
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        holder.propic.setImageResource(currentItem.image)
        holder.company.text = currentItem.company
        holder.vacancy.text = currentItem.vacancy
        holder.mode.text = currentItem.status
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val mode : TextView = itemView.findViewById(R.id.mode)

    }
}