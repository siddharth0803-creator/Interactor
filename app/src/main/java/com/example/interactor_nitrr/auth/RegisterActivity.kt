package com.example.interactor_nitrr.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.interactor_nitrr.MainActivity
import com.example.interactor_nitrr.R
import com.example.interactor_nitrr.databinding.ActivityRegisterBinding
import com.example.interactor_nitrr.model.UserModel
import com.example.interactor_nitrr.utils.Config
import com.example.interactor_nitrr.utils.Config.hideDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    private var imageUri : Uri?=null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it

        binding.userImage.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.saveData.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        if(binding.userName.text.toString().isEmpty()
            || binding.userBranch.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || imageUri == null
            ){
            Toast.makeText(this,"Please Enter all Fields",Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage()
        }
    }

    private fun uploadImage(){
        Config.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener{
                    hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                hideDialog()
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {
        val data = hashMapOf<String, Any>(
            "name" to binding.userName.text.toString(),
            "image" to imageUrl.toString(),
            "email" to binding.userEmail.text.toString(),
            "branch" to binding.userBranch.text.toString(),
            "number" to FirebaseAuth.getInstance().currentUser!!.phoneNumber!!.toString()
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .updateChildren(data).addOnCompleteListener{
                hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this,InterestActivity::class.java))
                    finish()
                    Toast.makeText(this,"User register Successful",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
}