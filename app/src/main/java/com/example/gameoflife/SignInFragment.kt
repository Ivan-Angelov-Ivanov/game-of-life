package com.example.gameoflife

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gameoflife.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private var binding: FragmentSignInBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSignInBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { SignInFragmentArgs.fromBundle(it) }
        val selectedEmail = args?.email

        binding?.apply {
            if(selectedEmail != null) {
                inputEmail.setText(selectedEmail)
                inputEmail.isEnabled = false
            }
            signInBackBtn.setOnClickListener { backToUserList() }
            signInBtn.setOnClickListener {
                when {
                    TextUtils.isEmpty(inputEmail.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show()
                    }

                    TextUtils.isEmpty(inputPassword.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // create an instance and create a register a user with email and password
                        val email: String = inputEmail.text.toString().trim { it <= ' ' }
                        val password: String = inputPassword.text.toString().trim { it <= ' ' }
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        activity,
                                        "Logged in successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    proceedToGame()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    }
                }
            }
        }
    }

    private fun backToUserList() {
        findNavController().navigate(R.id.action_signInFragment_to_userListFragment)
    }

    private fun proceedToGame() {
        val intent = Intent(activity, PixelGridActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}