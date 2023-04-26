package com.example.nekoshigoto

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.nekoshigoto.databinding.FragmentSubmitApplicationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.random.Random

class SubmitApplicationFragment : Fragment() {

    private lateinit var binding : FragmentSubmitApplicationBinding
    private lateinit var storage : FirebaseStorage
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var progressDialog : ProgressDialog
    private lateinit var pdfUri: Uri
    private var chgPDF = false
    private lateinit var dialog : AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubmitApplicationBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(JobSeekerViewModel::class.java)
        val jobSeeker = viewModel.getJobSeeker()
        val qualification = viewModel.getQualification()
        val jobid = arguments?.getString("jobid").toString()
        val view = binding.root
        binding.apply{
            editTextName.isEnabled = false
            editTextName.setText(jobSeeker.fname+ " " + jobSeeker.lname)
            binding.editTextResume.tag = qualification.resumeURl
            if(qualification.resumeURl=="") {
                editTextResume.setText("No resume found, click here to upload")
            }
            else{
                editTextResume.setText("Resume found, click here to upload another resume")
            }
            editTextResume.setOnClickListener{
                dialog = AlertDialog.Builder(requireContext())

                dialog.setTitle("Upload resume ")
                    .setMessage("Are you sure to upload a resume?")
                    .setCancelable(true)
                    .setPositiveButton("Upload"){dialogInterface,it->
                        selectPdf()
                    }.setNegativeButton("Cancel"){ dialogInterface,it->

                    }.show()

            }
        }

        binding.button2.setOnClickListener {
            if(binding.editTextCover.text.toString() == "") {
                Toast.makeText(requireContext(), "Please fill up the cover letter field", Toast.LENGTH_LONG).show()
            } else{
                val oldResume = binding.editTextResume.tag as String
                val cover = binding.editTextCover.text.toString()
                if(chgPDF){
                    uploadPdf { pdf ->
                        val application = AppDetails(cover, jobSeeker.email, pdf, "P")
                        db.collection("Vacancy").document(jobid)
                            .collection("Application").document(jobSeeker.email).set(application)

                        Toast.makeText(requireContext(), "Successfully submit application", Toast.LENGTH_SHORT).show()
                        view.findNavController().navigate(R.id.action_submitApplicationFragment_to_homeFragment)
                    }
                }else{
                    if(oldResume == ""){
                        dialog = AlertDialog.Builder(requireContext())

                        dialog.setTitle("Resume is missing... ")
                            .setMessage("Do you want to continue to update your qualification without resume? ")
                            .setCancelable(true)
                            .setPositiveButton("continue"){dialogInterface,it->
                                val application = AppDetails(cover, jobSeeker.email, oldResume, "P")
                                db.collection("Vacancy").document(jobid)
                                    .collection("Application").document(jobSeeker.email).set(application)
                                Toast.makeText(requireContext(), "Successfully submit application", Toast.LENGTH_SHORT).show()
                                view.findNavController().navigate(R.id.action_submitApplicationFragment_to_homeFragment)
                            }
                            .setNegativeButton("Cancel"){dialogInterface,it->
                                Toast.makeText(requireContext(), "Please fill up all the fields", Toast.LENGTH_SHORT).show()
                            }
                            .show()

                    }else{
                        val application = AppDetails(cover, jobSeeker.email, oldResume, "P")
                        db.collection("Vacancy").document(jobid)
                            .collection("Application").document(jobSeeker.email).set(application)
                        Toast.makeText(requireContext(), "Successfully submit application", Toast.LENGTH_SHORT).show()
                        view.findNavController().navigate(R.id.action_submitApplicationFragment_to_homeFragment)
                    }

                }
            }
        }

        binding.removeButton.setOnClickListener {
            dialog = AlertDialog.Builder(requireContext())

            dialog.setTitle("Remove resume ")
                .setMessage("Are you sure to remove the current resume?")
                .setCancelable(true)
                .setPositiveButton("Remove"){dialogInterface,it->
                    if(binding.editTextResume.tag as String == ""){
                        Toast.makeText(requireContext(), "No resume founded", Toast.LENGTH_LONG).show()
                    }
                    else{
                        binding.editTextResume.tag = ""
                        binding.editTextResume.text = "Resume removed, click here to upload"
                        chgPDF = false
                    }
                    Toast.makeText(requireContext(),"Please click the edit button to save the changes", Toast.LENGTH_LONG).show()
                }.setNegativeButton("Cancel"){ dialogInterface,it->

                }.show()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }
    private fun uploadPdf(callback: (String) -> Unit){
        storage = FirebaseStorage.getInstance()
        val reference = storage.reference.child("Resume").child(Date().time.toString() + randomString(8) + ".pdf")
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("File is uploading...")
        progressDialog.show()
        reference.putFile(pdfUri).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {
                    progressDialog.dismiss()
                    val pdf = it.toString()
                    callback(pdf)
                }
            }
        }.addOnProgressListener {
            val progress = (100.0*it.bytesTransferred/it.totalByteCount)
            progressDialog.setMessage("File is uploading... ${progress.toInt()}%")
        }
    }
    private fun randomString(n: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..n)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // For loading PDF
        when (requestCode) {
            12 -> if (resultCode == AppCompatActivity.RESULT_OK) {

                pdfUri = data?.data!!
                val uriString: String = pdfUri.toString()
                var pdfName: String? = null
                if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        myCursor = requireContext().applicationContext!!.contentResolver.query(pdfUri, null, null, null, null)
                        if (myCursor != null && myCursor.moveToFirst()) {
                            val nameIndex = myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (nameIndex >= 0) {
                                pdfName = myCursor.getString(nameIndex)
                            }
                            chgPDF = true
                            binding.editTextResume.text = pdfName
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home?
        activity?.hideBottomNav()
    }

}