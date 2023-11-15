package com.example.interactor_nitrr.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.interactor_nitrr.MainActivity
import com.example.interactor_nitrr.R
import com.example.interactor_nitrr.databinding.ActivityLginBinding
import com.example.interactor_nitrr.utils.Config
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLginBinding

    val auth= FirebaseAuth.getInstance()

    private var verificationId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sendOtp1.setOnClickListener{
            if (binding.userNumber1.text!!.isEmpty()){
                binding.userNumber1.error = "Please enter your number"
            }
            else{
                sendOtp(binding.userNumber1.text.toString())
            }
        }

        binding.verifyOtp.setOnClickListener{
            if (binding.userOtp.text!!.isEmpty()){
                binding.userOtp.error = "Please enter your OTP"
            }
            else{
                verifyOtp(binding.userOtp.text.toString())
            }
        }
    }

    private fun sendOtp(number: String){
        //binding.sendOtp1.showLoadingButton()
        Config.showDialog(this)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //binding.sendOtp1.showNormalButton()
                Config.hideDialog()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LginActivity.verificationId = verificationId

               // binding.sendOtp1.showNormalButton()
                Config.hideDialog()
                binding.numberLayout.visibility=GONE
                binding.otpLayout.visibility=VISIBLE
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91${number}") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String){
        //binding.sendOtp1.showLoadingButton()
        Config.showDialog(this)
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                //binding.sendOtp1.showNormalButton()
                if (task.isSuccessful) {

                    checkUserExist(binding.userNumber1.text.toString())


                    //startActivity(Intent(this,MainActivity::class.java))
                    //finish()
                } else {
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }
            }
    }

    private fun checkUserExist(number: String){
        FirebaseDatabase.getInstance().getReference("users").child("+91${number}")
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Config.hideDialog()
                    Toast.makeText(this@LginActivity,error.message , Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        Config.hideDialog()
                        Log.d("LginActivity", "User exists, starting MainActivity")
                        startActivity(Intent(this@LginActivity,MainActivity::class.java))
                        finish()
                    } else {
                        Log.d("LginActivity", "User does not exist, starting RegisterActivity")
                        startActivity(Intent(this@LginActivity,RegisterActivity::class.java))
                        finish()
                    }
                }

            })
    }
}