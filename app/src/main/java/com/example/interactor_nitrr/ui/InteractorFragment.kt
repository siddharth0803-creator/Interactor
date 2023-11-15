package com.example.interactor_nitrr.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interactor_nitrr.R
import com.example.interactor_nitrr.adapter.userAdapter
import com.example.interactor_nitrr.databinding.FragmentInteractorBinding
import com.example.interactor_nitrr.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Math.sqrt

class InteractorFragment : Fragment() {

    private lateinit var binding: FragmentInteractorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInteractorBinding.inflate(layoutInflater)

        binding.cardHolder.layoutManager = LinearLayoutManager(requireContext())

        binding.btnSwipe.setOnClickListener {
            val layoutManager = binding.cardHolder.layoutManager
            val firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val nextItemPosition = firstVisibleItemPosition + 1

            if (nextItemPosition < layoutManager.itemCount) {
                layoutManager.scrollToPosition(nextItemPosition)
            }
            else{
                Toast.makeText(requireContext(),"No Next user to show",Toast.LENGTH_SHORT).show()
            }
        }

        getData()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getData(){
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("test","onDataChange: ${snapshot.toString()}")
                    if(snapshot.exists()){
                        var list = arrayListOf<UserModel>()
                        val userInterests=mutableMapOf<String, ArrayList<String>>()
                        val size = snapshot.childrenCount
                        for (data in snapshot.children){
                            val model= data.getValue(UserModel::class.java)
                            list!!.add(model!!)
                            userInterests[model.number!!]=model.interests!!
                        }
                        val generatedList= generateRecommendations(userInterests,list,size)
                        var newList = arrayListOf<UserModel>()
                        for(element in generatedList){
                            newList.add(element)
                        }
                        newList.shuffle()
                        binding.cardHolder.adapter= userAdapter(requireContext(),newList!!)
                    }
                    else{
                        Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
                }

            })
    }


    /*
    programming -> 0
    Health -> 1
    Sports -> 2
    Dancing ->3
    Singing ->4
    Yoga -> 5
    Art -> 6
    Movies -> 7
    Drawing -> 8
    Problem Solving -> 9
    CP -> 10
    Anime -> 11
    Research -> 12
    Acting -> 13
    Blogging -> 14
    Music -> 15
     */
    private fun convertToMatrix(user:ArrayList<String>): ArrayList<Int>{
        val list = arrayListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        for(data in user){
            if(data == "programming"){
                list[0]=1
            }
            if(data == "Health"){
                list[1]=1
            }
            if(data == "Sports"){
                list[2]=1
            }
            if(data == "Dancing"){
                list[3]=1
            }
            if(data == "Singing"){
                list[4]=1
            }
            if(data == "Yoga"){
                list[5]=1
            }
            if(data == "Art"){
                list[6]=1
            }
            if(data == "Movies"){
                list[7]=1
            }
            if(data == "Drawing"){
                list[8]=1
            }
            if(data == "Problem Solving"){
                list[9]=1
            }
            if(data == "CP"){
                list[10]=1
            }
            if(data == "Anime"){
                list[11]=1
            }
            if(data == "Research"){
                list[12]=1
            }
            if(data == "Acting"){
                list[13]=1
            }
            if(data == "Blogging"){
                list[14]=1
            }
            if(data == "Music"){
                list[15]=1
            }
        }
        return list
    }

    private fun calculateSimilarity(user1: ArrayList<String>, user2: ArrayList<String>): Double {
        val value1 = convertToMatrix(user1)
        val value2 = convertToMatrix(user2)

        var multi = 0.0
        var SquareValue1 = 0.0
        var SquareValue2 = 0.0


        for(index in 0..15){
            multi+=(value1[index]*value2[index])
            SquareValue1+=(value1[index]*value1[index])
            SquareValue2+=(value2[index]*value2[index])
        }

        val cosineDeno= kotlin.math.sqrt(SquareValue1) * kotlin.math.sqrt(SquareValue2)
        val cosineSimilarity = multi/cosineDeno
        return cosineSimilarity
    }

    private fun generateRecommendations(userInterests: MutableMap<String, ArrayList<String>>, list: ArrayList<UserModel>, size : Long): List<UserModel> {
        val targetNumber= FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()
        val targetUser = userInterests[targetNumber]
        val filteredList = list.filter { it.number != targetNumber }
        return filteredList
            .sortedByDescending { calculateSimilarity(it.interests!!, targetUser!!) }
    }
}

