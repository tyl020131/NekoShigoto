
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.Job
import com.example.nekoshigoto.R
import com.example.nekoshigoto.Vacancy
import com.google.android.material.imageview.ShapeableImageView

class VacancyAdapter(private val vacancyList : ArrayList<Vacancy>,private val navigator:NavController) :
    RecyclerView.Adapter<VacancyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = vacancyList[position]
        val imgUri = currentItem.image.toUri().buildUpon().scheme("https").build()
        holder.propic.load(imgUri)
        //holder.propic.setImageResource(currentItem.image)
        holder.company.text = currentItem.companyName
        holder.vacancy.text = currentItem.position
        holder.numOfApp.text = "${currentItem.numOfApp} people has applied for this vacancy"
        holder.status.text = currentItem.status
        if(currentItem.status == "Inactive")
            holder.status.setTextColor(ContextCompat.getColor(holder.status.context, R.color.color8))

        holder.cont.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("jobname",currentItem.vacancyid)
            navigator.navigate(R.id.action_vacancyFragment_to_vacancyDetailFragment,bundle)
        }
    }

    fun getCurrentData() : ArrayList<Vacancy>{
        return this.vacancyList
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val cont : ConstraintLayout = itemView.findViewById(R.id.jobb)
        val propic :ShapeableImageView = itemView.findViewById(R.id.profilepic)
        val company : TextView = itemView.findViewById(R.id.company_name)
        val vacancy : TextView = itemView.findViewById(R.id.vacancy)
        val status : TextView = itemView.findViewById(R.id.status)
        val numOfApp : TextView = itemView.findViewById(R.id.numOfApp)
    }
}