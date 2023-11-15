package com.example.interactor_nitrr.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.interactor_nitrr.R
import com.example.interactor_nitrr.activity.ChatWithAiActivity
import com.example.interactor_nitrr.activity.EditProfileActivity
import com.example.interactor_nitrr.auth.LginActivity
import com.example.interactor_nitrr.databinding.FragmentProfileBinding
import com.example.interactor_nitrr.model.UserModel
import com.example.interactor_nitrr.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Config.showDialog(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .get().addOnSuccessListener {
                if(it.exists()){
                    val data= it.getValue(UserModel::class.java)

                    binding.name.setText(data!!.name.toString())
                    binding.email.setText(data!!.email.toString())
                    binding.branch.setText(data!!.branch.toString())
                    binding.number.setText(data!!.number.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.profile).into(binding.showImage)

                    Config.hideDialog()
                }
            }

            binding.logout.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(requireContext(),LginActivity::class.java))
                requireActivity().finish()
            }

            binding.editProfile.setOnClickListener{
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }

            binding.AskBot.setOnClickListener{
                startActivity(Intent(requireContext(), ChatWithAiActivity::class.java))
            }

        return binding.root
    }
}