package com.example.nekoshigoto

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.nekoshigoto.databinding.FragmentCompanyProfileBinding
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.setOnCountrySelected
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.show
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.random.Random

class CompanyProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompanyProfileBinding
    private lateinit var selectedImg : Uri
    private var chgImg : Boolean = false
    private var error : Boolean = false
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var storage : FirebaseStorage
    private lateinit var email : String
    private lateinit var dialog : AlertDialog.Builder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        storage = FirebaseStorage.getInstance()
        var sh : SharedPreferences = requireActivity().getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        binding = FragmentCompanyProfileBinding.inflate(inflater, container, false)

        binding.apply {
            editLayout.visibility = View.GONE
            errorTextContact.visibility = View.GONE
            errorTextAddress.visibility = View.GONE
            errorTextCountry.visibility = View.GONE
            errorTextState.visibility = View.GONE
            errorTextBusiness.visibility = View.GONE
            errorTextProfile.visibility = View.GONE
        }

        val viewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        var company : Company = viewModel.getCompany()

        binding.apply {
            //insert data for view layout
            textName.text = company?.name
            textEmail.text = company?.email
            email = company.email
            textAddress.text = company?.address
            textContact.text = company?.contactNo
            textCountry.text = company?.country
            textState.text = company?.state
            textBusiness.text = company?.business
            Glide.with(requireContext())
                .load(company?.profilePic)
                .into(binding.profileImage)

            //insert data for edit layout
            editTextContact.setText(company?.contactNo)
            editTextCountry.text = company?.country
            editTextState.setText(company?.state)
            editTextAddress.setText(company?.address)
            editTextBusiness.setText(company?.business)

            Glide.with(requireContext())
                .load(company?.profilePic)
                .into(binding.uploadImage)

            binding.uploadImage.tag = company?.profilePic

        }

        binding.editButton.setOnClickListener {
            binding.scrollview.smoothScrollTo(0, 0)
            binding.editLayout.visibility = View.VISIBLE
            binding.viewLayout.visibility = View.GONE
        }

        binding.cancelButton.setOnClickListener {
            binding.scrollview.smoothScrollTo(0, 0)
            binding.editLayout.visibility = View.GONE
            binding.viewLayout.visibility = View.VISIBLE
        }

        binding.editTextCountry.setOnClickListener{
            CountrySelectionDialog().create(requireContext())
                .show()?.setOnCountrySelected {
                    binding.editTextCountry.text = it.name
                }
        }

        binding.uploadImage.setOnClickListener{

            val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            myAlertDialog.setTitle("Select Image")
            myAlertDialog.setCancelable(true)
            myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
                when {

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
                }
            })
            myAlertDialog.show()

        }

        binding.saveButton.setOnClickListener {
            error = false

            val contactNoT = binding.editTextContact
            val addressT = binding.editTextAddress
            val countryT = binding.editTextCountry
            val stateT = binding.editTextState
            val businessT = binding.editTextBusiness

            checkAddress(addressT.text.toString())
            checkBusiness(businessT.text.toString())
            checkContact(contactNoT.text.toString())
            checkCountry(countryT.text.toString())
            checkState(stateT.text.toString())

            if(error)
            {
                Toast.makeText(requireContext(), "Please check the error message", Toast.LENGTH_SHORT).show()
            }
            else
            {
                dialog = AlertDialog.Builder(requireContext())

                dialog.setTitle("Edit Confirmation ")
                    .setMessage("Are you sure to edit profile? ")
                    .setCancelable(true)
                    .setPositiveButton("Edit"){dialogInterface,it->
                        val loadedUrl = binding.uploadImage.tag as String
                        val contactNo = contactNoT.text.toString()
                        val country = countryT.text.toString()
                        val state = stateT.text.toString()
                        val address = addressT.text.toString()
                        val business = businessT.text.toString()
                        var name = binding.textName.text.toString()

                        if(chgImg){
                            //after changing profile pic
                            uploadImage { imageUrl ->
                                val user = Company(name, email, contactNo, address, country, state, imageUrl, business, "A")
                                val imageName = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
                                db.collection("Company").document(email).set(user)
                                val imgRef = FirebaseStorage.getInstance().getReference().child("Company/$imageName")
                                imgRef.delete()
                                viewModel.setCompany(user)
                            }
                        } else{
                            //no chg profile pic
                            val user = Company(name, email, contactNo, address, country, state, loadedUrl, business, "A")
                            db.collection("Company").document(email).set(user)
                            viewModel.setCompany(user)
                        }
                        requireView().findNavController().navigate(R.id.action_companyProfileFragment_self)
                        Toast.makeText(requireContext(), "Successfully Edit Profile", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel"){dialogInterface,it->

                    }
                    .show()
            }
        }
        return binding.root
    }

    private fun checkContact(contactNo:String){
        val regex = "^01\\d-\\d{7,8}$".toRegex()
        if(contactNo.isEmpty())
        {
            binding.errorTextContact.visibility = View.VISIBLE
            binding.errorTextContact.text = "Please enter your contact number"
            error = true
        }
        else if(!contactNo.matches(regex))
        {
            binding.errorTextContact.visibility = View.VISIBLE
            binding.errorTextContact.text = "Invalid Contact Number (xxx-xxxxxxxx)"
            error = true
        }
        else{
            binding.errorTextContact.visibility = View.GONE
        }
    }

    private fun checkCountry(country:String): Boolean {
        return if(country=="Click here to select country"){
            binding.errorTextCountry.visibility = View.VISIBLE
            binding.errorTextCountry.text = "Please select your country"
            error = true
            true
        }
        else{
            binding.errorTextCountry.visibility = View.GONE
            false
        }
    }

    private fun checkBusiness(business:String){
        if(business.isEmpty()){
            binding.errorTextBusiness.visibility = View.VISIBLE
            binding.errorTextBusiness.text = "Please enter your company's business"
            error = true
        }
        else{
            binding.errorTextBusiness.visibility = View.GONE
        }
    }

    private fun checkAddress(address:String){
        if(address.isEmpty()){
            binding.errorTextAddress.visibility = View.VISIBLE
            binding.errorTextAddress.text = "Please enter your company's business"
            error = true
        }
        else{
            binding.errorTextAddress.visibility = View.GONE
        }
    }

    private fun checkState(state:String){
        val regex = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$".toRegex()
        if(state.isEmpty()){
            binding.errorTextState.visibility = View.VISIBLE
            binding.errorTextState.text = "Please enter your State"
            error = true
        }
        else if(!state.matches(regex)) {
            binding.errorTextState.visibility = View.VISIBLE
            binding.errorTextState.text = "Invalid State"
            error = true
        }
        else{
            binding.errorTextState.visibility = View.GONE
        }
    }

    private fun uploadImage(callback: (String) -> Unit) {
        val reference = storage.reference.child("Company").child(Date().time.toString() + randomString(8))
        reference.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    callback(imageUrl)
                }
            }
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

        when(requestCode){
            2404 -> if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                selectedImg = data?.data!!
                binding.uploadImage.setImageURI(selectedImg)
                chgImg = true

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.company_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePasswordFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
            R.id.logout -> {
                startActivity(Intent(requireContext(), Logout::class.java))
            }
            R.id.companyEmailTemplateFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.showBottomNav()
        activity?.chgTitle("My Profile")
    }

}