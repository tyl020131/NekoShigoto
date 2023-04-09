
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.Application
import com.example.nekoshigoto.R
import com.google.android.material.imageview.ShapeableImageView

class ModeAdapter(private val modeList : ArrayList<String>) :
    RecyclerView.Adapter<ModeAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.filter_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = modeList[position];
        holder.mode_item.text = currentItem.toString()

        holder.mode_item.setOnClickListener {
            if(it.getTag() == "NotSelected"){
                it.tag = "Selected";
                it.setBackgroundDrawable(it.resources.getDrawable(R.drawable.filter_item_selected) )
                holder.mode_item.setTextColor(it.resources.getColor(R.color.white))
            }
            else{
                it.tag = "NotSelected";
                it.background = it.resources.getDrawable(R.drawable.filter_item_notselected)
                holder.mode_item.setTextColor(it.resources.getColor(R.color.black))
            }
        }

    }

    override fun getItemCount(): Int {
        return modeList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mode_item :TextView = itemView.findViewById(R.id.filter_item)


    }
}