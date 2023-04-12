
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.Application
import com.example.nekoshigoto.R
import com.google.android.material.imageview.ShapeableImageView

class SingleModeAdapter(private val modeList : ArrayList<String>) :
    RecyclerView.Adapter<SingleModeAdapter.MyViewHolder>() {

    private var selectedMode : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.filter_item,parent,false)
        val newHolder : MyViewHolder = MyViewHolder(itemView)
        newHolder.mode_item.setOnClickListener {
            it.tag = "NotSelected";
            it.background = it.resources.getDrawable(R.drawable.filter_item_notselected)

        }
        return newHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = modeList[position];
        holder.mode_item.text = currentItem.toString()

        holder.mode_item.setOnClickListener {
            if(it.getTag() == "NotSelected"){
                it.tag = "Selected";
                it.setBackgroundDrawable(it.resources.getDrawable(R.drawable.filter_item_selected) )
                holder.mode_item.setTextColor(it.resources.getColor(R.color.white))
                selectedMode = currentItem.toString()
            }
            else{
                it.tag = "NotSelected";
                it.background = it.resources.getDrawable(R.drawable.filter_item_notselected)
                holder.mode_item.setTextColor(it.resources.getColor(R.color.black))
            }
        }

    }

    fun getSelectedMode() : String{
        return selectedMode
    }

    override fun getItemCount(): Int {
        return modeList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mode_item :TextView = itemView.findViewById(R.id.filter_item)




    }
}