package com.example.interactor_nitrr.auth
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.interactor_nitrr.MainActivity
import com.example.interactor_nitrr.R
import com.example.interactor_nitrr.databinding.ActivityInterestBinding
import com.example.interactor_nitrr.model.UserModel
import com.example.interactor_nitrr.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class InterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterestBinding

    private var list : ArrayList<String>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list= ArrayList()
        populateList()

        binding.submit.setOnClickListener{
            validateData()
        }

    }

    private fun validateData() {
        if(list!!.size>=5){
            storeData()
        }
        else{
            Toast.makeText(this,"Please Select at least 5 interests",Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateList() {

        if(list==null){
            list=ArrayList()
        }

        binding.Acting.setOnClickListener{
            list!!.add("Acting")
            val button1 = findViewById<Button>(R.id.Acting)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Health.setOnClickListener{
            list!!.add("Health")
            val button1 = findViewById<Button>(R.id.Health)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Sports.setOnClickListener{
            list!!.add("Sports")
            val button1 = findViewById<Button>(R.id.Sports)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Blogging.setOnClickListener{
            list!!.add("Blogging")
            val button1 = findViewById<Button>(R.id.Blogging)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Research.setOnClickListener{
            list!!.add("Research")
            val button1 = findViewById<Button>(R.id.Research)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.CP.setOnClickListener{
            list!!.add("CP")
            val button1 = findViewById<Button>(R.id.CP)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.ProblemSolving.setOnClickListener{
            list!!.add("Problem Solving")
            val button1 = findViewById<Button>(R.id.ProblemSolving)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Programming.setOnClickListener{
            list!!.add("Programming")
            val button1 = findViewById<Button>(R.id.Programming)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Music.setOnClickListener{
            list!!.add("Music")
            val button1 = findViewById<Button>(R.id.Music)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Movies.setOnClickListener{
            list!!.add("Movies")
            val button1 = findViewById<Button>(R.id.Movies)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Singing.setOnClickListener{
            list!!.add("Singing")
            val button1 = findViewById<Button>(R.id.Singing)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Drawing.setOnClickListener{
            list!!.add("Drawing")
            val button1 = findViewById<Button>(R.id.Drawing)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Dancing.setOnClickListener{
            list!!.add("Dancing")
            val button1 = findViewById<Button>(R.id.Dancing)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Yoga.setOnClickListener{
            list!!.add("Yoga")
            val button1 = findViewById<Button>(R.id.Yoga)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Art.setOnClickListener{
            list!!.add("Art")
            val button1 = findViewById<Button>(R.id.Art)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

        binding.Anime.setOnClickListener{
            list!!.add("Anime")
            val button1 = findViewById<Button>(R.id.Anime)
            button1.setTextColor(ContextCompat.getColor(this, R.color.red))

        }

    }

    private fun storeData() {
        Config.showDialog(this)
        val interests = list
        val interestsAsAny: List<Any> = interests!!.map { it as Any }

        val data = hashMapOf<String, Any>(
            "interests" to interestsAsAny
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .updateChildren(data).addOnCompleteListener{
                Config.hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(this,"Interest Saved Successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}