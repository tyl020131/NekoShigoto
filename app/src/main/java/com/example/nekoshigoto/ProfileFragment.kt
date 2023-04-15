package com.example.nekoshigoto

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.nekoshigoto.databinding.FragmentProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Disable the up button
        setHasOptionsMenu(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
        
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.apply {
            saveButton.visibility = View.GONE
            editLayout.visibility = View.GONE
            cancelButton.visibility = View.GONE

            textName.isEnabled = false
            textGender.isEnabled = false
            textNationality.isEnabled = false
            textDOB.isEnabled = false
            textContact.isEnabled = false
            textIcNo.isEnabled = false
            textCountry.isEnabled = false
            textState.isEnabled = false
        }

        binding.editButton.setOnClickListener {
            binding.apply {
                scrollView2.smoothScrollTo(0, 0)
                saveButton.visibility = View.VISIBLE
                editLayout.visibility = View.VISIBLE
                cancelButton.visibility = View.VISIBLE

                viewLayout.visibility = View.GONE
                editButton.visibility = View.GONE
                qualificationButton.visibility = View.GONE
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.apply {
                scrollView2.smoothScrollTo(0, 0)
                saveButton.visibility = View.GONE
                editLayout.visibility = View.GONE
                cancelButton.visibility = View.GONE

                viewLayout.visibility = View.VISIBLE
                editButton.visibility = View.VISIBLE
                qualificationButton.visibility = View.VISIBLE
            }
        }

        binding.qualificationButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_qualificationFragment)
        }

        binding.uploadImage.setOnClickListener{

            val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            myAlertDialog.setTitle("Select Image")
            myAlertDialog.setCancelable(true)
            myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
                when {
                    // Select "Choose from Gallery" to pick image from gallery
                    choice[item] == "Choose from Gallery" -> {
                        ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*"))
                            .crop()	    			        //Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start()
                    }
                    // Select "Take Photo" to take a photo
                    choice[item] == "Take Photo" -> {
                        ImagePicker.with(this).cameraOnly().crop().start()
                    }
                    // Select "Cancel" to cancel the task
                    choice[item] == "Cancel" -> {
                    }
                }
            })
            myAlertDialog.show()

        }

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            2404 -> if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                binding.uploadImage.setImageURI(uri)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

}