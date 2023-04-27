
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.JobSeeker
import com.example.nekoshigoto.R
import com.example.nekoshigoto.databinding.JobseekerFilterDialogBinding
import com.example.nekoshigoto.databinding.UserListBinding
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class UserAdapter(private val userList : ArrayList<JobSeeker>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position];
        val imgUri = currentItem.profilePic.toUri().buildUpon().scheme("https").build()
        holder.propic.load(imgUri)
        holder.name.text = currentItem.fname + " " + currentItem.lname

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val dob = LocalDate.parse(currentItem.dob, formatter)
        val currentDate: LocalDate = LocalDate.now()
        val age: Int = Period.between(dob, currentDate).getYears()
        holder.age.text = "$age Years Old, ${currentItem.nationality}"

        holder.location.text = currentItem.state + ", " + currentItem.country

        var list = currentItem.workingMode.split(", ")
        list.forEach {
            if(it == holder.freelance.text.toString())
                holder.freelance.visibility = View.VISIBLE
            else if(it == holder.partTime.text.toString())
                holder.partTime.visibility = View.VISIBLE
            else if(it == holder.fullTime.text.toString())
                holder.fullTime.visibility = View.VISIBLE
        }

        if(currentItem.gender == "Male"){
            holder.gender.setImageResource(R.drawable.male)
        }else{
            holder.gender.setImageResource(R.drawable.female)
        }

        holder.jobb.setOnClickListener {
            val bundle = bundleOf("dataKey" to currentItem.email)
            it.findNavController().navigate(R.id.action_viewUserFragment_to_userDetailFragment, bundle)
        }


    }

    class MyViewHolder(private val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root){
        val propic :ShapeableImageView = binding.profilepic
        val name : TextView = binding.name
        val age : TextView = binding.age
        val location : TextView = binding.location
        val gender : ImageButton = binding.gender
        val partTime = binding.partTime
        val fullTime = binding.fullTime
        val freelance = binding.freelance
        val jobb = binding.jobb
    }
}