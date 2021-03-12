package com.example.gameoflife

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameoflife.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {
    private var binding: FragmentUserListBinding? = null
    private var recyclerView: RecyclerView? = null
    private val signInFragment = SignInFragment()
    private val args = Bundle()

    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentUserListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding?.listOfUsers
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = UserAdapter { selectedUser ->
            binding?.apply {
                continueBtn.isEnabled = true
            }
            args.putString("email", selectedUser.email)
            signInFragment.arguments = args
        }
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory((activity?.application as UsersApplication).repository)
        ).get(UserViewModel::class.java)
        userViewModel.allUsers.observe(viewLifecycleOwner, { users ->
            (recyclerView?.adapter as UserAdapter).setUsers(users)
        })

//        val ref = FirebaseDatabase.getInstance().getReference("users")
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (data in snapshot.children) {
//                    val user = data.getValue(User::class.java)
//                    list?.add(user!!)
//                }
//
//                recyclerView?.adapter = UserAdapter(list!!) {
//                    binding?.apply {
//                        continueBtn.isEnabled = true
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(activity, "Something went wrong: $error", Toast.LENGTH_SHORT).show()
//            }
//
//        })

        binding?.apply {
            continueBtn.setOnClickListener { proceedToSignIn() }
            signUpBtn.setOnClickListener { proceedToSignUp() }
        }
    }

    private fun proceedToSignIn() {
        val action = args.getString("email")?.let {
            UserListFragmentDirections.actionUserListFragmentToSignInFragment(it)
        }
        if (action != null) {
            findNavController().navigate(action)
        }
    }

    private fun proceedToSignUp() {
        findNavController().navigate(R.id.action_userListFragment_to_signUpFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
