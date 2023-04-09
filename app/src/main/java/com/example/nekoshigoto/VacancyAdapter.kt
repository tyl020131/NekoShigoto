
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.Job
import com.example.nekoshigoto.R
import com.google.android.material.imageview.ShapeableImageView

class VacancyAdapter(private val vacancyList : ArrayList<Job>) :
    RecyclerView.Adapter<VacancyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = vacancyList[position];
        holder.propic.setImageResource(currentItem.image)
        holder.company.text = currentItem.company
        holder.vacancy.text = currentItem.vacancy
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val numOfApp : TextView = itemView.findViewById(R.id.numOfApp)
    }
}