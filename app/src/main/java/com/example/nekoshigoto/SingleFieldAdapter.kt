
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat.setTextAppearance
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.Application
import com.example.nekoshigoto.R
import com.google.android.material.imageview.ShapeableImageView
import org.w3c.dom.Text

class SingleFieldAdapter(private var fieldList : ArrayList<String>) :
    RecyclerView.Adapter<SingleFieldAdapter.MyViewHolder>() {

    private var selectedField : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.filter_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = fieldList[position];
        holder.mode_item.text = currentItem.toString()
        holder.mode_item.tag = "NotSelected"
        holder.mode_item.setOnClickListener {

            if(it.getTag() == "NotSelected"){
                it.tag = "Selected";
                it.setBackgroundDrawable(it.resources.getDrawable(R.drawable.filter_item_selected) )
                holder.mode_item.setTextColor(it.resources.getColor(R.color.white))
                selectedField = currentItem.toString()
            }
            else{
                it.tag = "NotSelected";
                it.background = it.resources.getDrawable(R.drawable.filter_item_notselected)
                holder.mode_item.setTextColor(it.resources.getColor(R.color.black))
            }



        }

    }

    override fun getItemCount(): Int {
        return fieldList.size
    }

   fun resetSelection(): Int {
        for(i in 0..fieldList.size-1){
            if(fieldList.get(i).toString() == selectedField){
                return i
            }
        }
        return 0
    }

    fun getSelectedField() : String{
        return selectedField
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val mode_item :TextView = itemView.findViewById(R.id.filter_item)



    }
}