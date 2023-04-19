package com.example.nekoshigoto

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.nekoshigoto.databinding.FragmentQualificationBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.random.Random

class QualificationFragment : Fragment() {

    private lateinit var binding : FragmentQualificationBinding
    private lateinit var pdfUri: Uri
    private lateinit var selectedField : String
    private lateinit var selectedEdu : String
    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private var chgPDF = false
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var storage : FirebaseStorage
    private lateinit var progressDialog : ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentQualificationBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        storage = FirebaseStorage.getInstance()
        email = auth.currentUser?.email.toString()

        binding.button2.visibility = View.GONE
        binding.cancelButton.visibility = View.GONE
        binding.fieldSpinner.visibility = View.GONE
        binding.educationLevelSpinner.visibility = View.GONE
        binding.editTextExp.isEnabled = false
        binding.editTextResume.setOnClickListener {
            viewPdf()
        }

        binding.editButton.setOnClickListener {
            binding.button2.visibility = View.VISIBLE
            binding.fieldSpinner.visibility = View.VISIBLE
            binding.educationLevelSpinner.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.VISIBLE

            binding.editButton.visibility = View.GONE
            binding.textEducation.visibility = View.GONE
            binding.textField.visibility = View.GONE

            binding.editTextResume.setOnClickListener {
                selectPdf()
            }
            binding.editTextResume.setText("Click here to upload resume")

            binding.editTextExp.isEnabled = true
        }

        binding.cancelButton.setOnClickListener {
            binding.button2.visibility = View.GONE
            binding.fieldSpinner.visibility = View.GONE
            binding.educationLevelSpinner.visibility = View.GONE
            binding.cancelButton.visibility = View.GONE

            binding.editButton.visibility = View.VISIBLE
            binding.textEducation.visibility = View.VISIBLE
            binding.textField.visibility = View.VISIBLE

            binding.editTextResume.setOnClickListener {
                viewPdf()
            }
            binding.editTextResume.setText("Click here to view resume")

            binding.editTextExp.isEnabled = false
        }

        //education spinner setup
        val educationArray = resources.getStringArray(R.array.education_array)
        val eduSpinner = binding.educationLevelSpinner
        val spinnerAdapter= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, educationArray) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    view.setTextColor(resources.getColor(R.color.color1))
                }
                return view
            }

        }


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eduSpinner.adapter = spinnerAdapter

        eduSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                selectedEdu = value
                if(value == educationArray[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }

        }

        var fieldArray =  resources.getStringArray(R.array.field_array)
        fieldArray = arrayOf("Select Select Your Field...") + fieldArray
        val fieldSpinner = binding.fieldSpinner
        val spinnerAdapter2= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, fieldArray) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    view.setTextColor(resources.getColor(R.color.color1))
                }
                return view
            }

        }


        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fieldSpinner.adapter = spinnerAdapter2

        fieldSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                selectedField = value
                if(value == fieldArray[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
        }
        db.collection("Qualification").document(email).get()
            .addOnSuccessListener {
                val qualification = it.toObject<Qualification>()  //convert the doc into object

                binding.apply {
                    for(i in educationArray.indices){
                        if(qualification?.education == educationArray[i]){
                            educationLevelSpinner.setSelection(i)
                            selectedEdu = educationArray[i]
                            break
                        }
                    }
                    textEducation.text = qualification?.education

                    for (i in fieldArray.indices) {
                        if (fieldArray[i] == qualification?.field) {
                            fieldSpinner.setSelection(i)
                            selectedField = fieldArray[i]
                            break
                        }
                    }
                    textField.text = qualification?.field

                    editTextExp.setText(qualification?.workingExp)

                    editTextResume.tag = qualification?.resumeURl
                }


            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }

        binding.button2.setOnClickListener {

            val loadedUrl = binding.editTextResume.tag as String
            if(eduSpinner.selectedItemPosition!=0 && fieldSpinner.selectedItemPosition!=0 &&
                binding.editTextExp.text.toString().isNotEmpty()){
                if(chgPDF){
                    uploadPdf { pdf ->
                        var newQuali = Qualification(selectedField, binding.editTextExp.text.toString(), selectedEdu, pdf)
                        val oldPDF = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
                        db.collection("Qualification").document(email).set(newQuali)
                        val pdfRef = FirebaseStorage.getInstance().getReference().child("Resume/$oldPDF")
                        pdfRef.delete()
                        Toast.makeText(requireContext(), "Successfully update your qualification", Toast.LENGTH_SHORT).show()
                        requireView().findNavController().navigate(R.id.action_qualificationFragment_self)
                    }
                }else{
                    //check is there any file
                    if(loadedUrl == "") {
                        //no file
                        Toast.makeText(requireContext(), "Please fill out all the fields", Toast.LENGTH_SHORT).show()
                    }else{
                        var newQuali = Qualification(selectedField, binding.editTextExp.text.toString(), selectedEdu, loadedUrl)
                        db.collection("Qualification").document(email).set(newQuali)
                        Toast.makeText(requireContext(), "Successfully update your qualification", Toast.LENGTH_SHORT).show()
                        requireView().findNavController().navigate(R.id.action_qualificationFragment_self)
                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "Please fill out all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home?
        activity?.hideBottomNav()
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    private fun downloadPdf(){
        var loadedUrl = binding.editTextResume.tag as String
        val oldPDF = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
        val pdfRef = FirebaseStorage.getInstance().getReference().child("Resume/$oldPDF")
        val localFile = File.createTempFile(oldPDF, ".pdf")

        pdfRef.getFile(localFile)
            .addOnSuccessListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.type = "application/pdf"
                intent.setData(loadedUrl.toUri())
                startActivity(intent)
            }
            .addOnFailureListener {
                // Handle any errors
            }
    }

    private fun viewPdf(){
        var loadedUrl = binding.editTextResume.tag as String
        val oldPDF = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
        val pdfRef = FirebaseStorage.getInstance().getReference().child("Resume/$oldPDF")
        val localFile = File.createTempFile(oldPDF, ".pdf")

        pdfRef.getFile(localFile)
            .addOnSuccessListener {
                val fileUri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", localFile)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fileUri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
            .addOnFailureListener {
                // Handle any errors
            }
    }
    private fun uploadPdf(callback: (String) -> Unit){
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


}