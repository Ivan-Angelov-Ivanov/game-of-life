package com.example.gameoflife

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gameoflife.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment() {
    private var binding: FragmentSignUpBinding? = null
    private lateinit var  userViewModel: UserViewModel
    private val signInFragment = SignInFragment()
    private val args = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory((activity?.application as UsersApplication).repository)
        ).get(UserViewModel::class.java)

        binding?.apply {
            signUpBackBtn.setOnClickListener { backToUserList() }
            signUpBtn.setOnClickListener {
                when {
                    TextUtils.isEmpty(signUpInputEmail?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(activity, "Please enter email", Toast.LENGTH_SHORT).show()
                    }

                    TextUtils.isEmpty(inputPassword.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(activity, "Please enter password", Toast.LENGTH_SHORT).show()
                    }

                    TextUtils.isEmpty(confirmPassword.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(activity, "Please confirm password", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        // create an instance and create a register a user with email and password
                        val email: String = signUpInputEmail?.text.toString().trim { it <= ' ' }
                        val password: String = inputPassword.text.toString().trim { it <= ' ' }
                        val password2: String = confirmPassword.text.toString().trim { it <= ' ' }

                        args.putString("email", email)
                        signInFragment.arguments = args

                        if (password != password2) {
                            Toast.makeText(activity, "Passwords dont match", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // val firebaseUser: FirebaseUser = task.result!!.user!!
                                        val ref =
                                            FirebaseDatabase.getInstance().getReference("users")
                                        val userId = ref.push().key
                                        val user = User(email, password)
                                        userViewModel.insert(user)
                                        ref.child(userId!!).setValue(user)
                                        Toast.makeText(
                                            activity,
                                            "Registration completed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        proceedToSignIn()
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
    }

    private fun backToUserList() {
        findNavController().navigate(R.id.action_signUpFragment_to_userListFragment)
    }

    private fun proceedToSignIn() {
        val action = args.getString("email")?.let {
            SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(it)
        }
        if (action != null) {
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}