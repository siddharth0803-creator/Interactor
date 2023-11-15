package com.example.interactor_nitrr.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.interactor_nitrr.MainActivity
import com.example.interactor_nitrr.R
import com.example.interactor_nitrr.databinding.ActivityEditProfileBinding
import com.example.interactor_nitrr.model.UserModel
import com.example.interactor_nitrr.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfileBinding

    private var imageUri : Uri?=null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it

        binding.showImage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        Config.showDialog(this@EditProfileActivity)
        setContentView(binding.root)

        showData()

        binding.showImage.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.editProfile.setOnClickListener{
            validateData()
        }

    }

    private fun validateData() {
        if(binding.name.text.toString().isEmpty()
            || binding.branch.text.toString().isEmpty()
            || binding.email.text.toString().isEmpty()
            || imageUri == null
        ){
            Toast.makeText(this,"Please Enter all Fields", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener{
                    Config.hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Config.hideDialog()
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {
        val data = UserModel(
            name= binding.name.text.toString(),
            image= imageUrl.toString(),
            email = binding.email.text.toString(),
            branch = binding.branch.text.toString(),
            number = FirebaseAuth.getInstance().currentUser!!.phoneNumber!!.toString()
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener{
                Config.hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(this,"Changes Made Successfully",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showData() {
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .get().addOnSuccessListener {
                if(it.exists()){
                    val data= it.getValue(UserModel::class.java)

                    binding.name.setText(data!!.name.toString())
                    binding.email.setText(data!!.email.toString())
                    binding.branch.setText(data!!.branch.toString())

                    Glide.with(this@EditProfileActivity).load(data.image).placeholder(R.drawable.profile).into(binding.showImage)

                    Config.hideDialog()
                }
            }
    }


}