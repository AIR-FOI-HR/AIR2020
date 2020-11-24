package hr.foi.academiclifestyle.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hr.foi.academiclifestyle.LoginActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {


    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater,R.layout.fragment_register,container,false)
        binding.lifecycleOwner= this

        binding.txtRegisterLogin.setOnClickListener{
            (activity as LoginActivity?)?.switchFragment(0)
        }

        binding.txtNameRegister.doAfterTextChanged {
            viewModel.setField(binding.txtNameRegister.text, 1)
        }

        binding.txtRegisterUsername.doAfterTextChanged {
            viewModel.setField(binding.txtRegisterUsername.text, 2)
        }

        binding.txtEmailRegister.doAfterTextChanged {
            viewModel.setField(binding.txtEmailRegister.text, 3)
        }

        binding.txtPasswordRegister.doAfterTextChanged {
            viewModel.setField(binding.txtPasswordRegister.text, 4)
        }
        binding.btnRegister.setOnClickListener(){
            viewModel.sendRegisterData()

        }
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it)
                (activity as LoginActivity?)?.switchFragment(0)
        })
        viewModel.responseType.observe(viewLifecycleOwner, Observer {
            when (it) {
                1 -> Toast.makeText(activity as LoginActivity?, "All fields must be filled in!", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(activity as LoginActivity?, "Email not in a valid format!", Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(activity as LoginActivity?, "Server Error, please try again!", Toast.LENGTH_SHORT).show()
                4 -> Toast.makeText(activity as LoginActivity?, "Successfully registered, you may log in!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}