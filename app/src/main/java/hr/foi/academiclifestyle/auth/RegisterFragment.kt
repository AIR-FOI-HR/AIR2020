package hr.foi.academiclifestyle.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        val login = binding.txtRegisterLogin
        val registerName = binding.txtNameRegister
        val registerUsername = binding.txtRegisterUsername
        val registerEmail = binding.txtEmailRegister
        val registerPassword =binding.txtPasswordRegister
        val register =binding.btnRegister

        login.setOnClickListener{
            (activity as LoginActivity?)?.switchFragment(0)
        }

        binding.txtNameRegister.doAfterTextChanged {
            viewModel.setUsername(binding.txtNameRegister.text)
        }

        binding.txtRegisterUsername.doAfterTextChanged {
            viewModel.setUsername(binding.txtRegisterUsername.text)
        }

        binding.txtEmailRegister.doAfterTextChanged {
            viewModel.setUsername(binding.txtEmailRegister.text)
        }

        binding.txtPasswordRegister.doAfterTextChanged {
            viewModel.setUsername(binding.txtPasswordRegister.text)
        }
        register.setOnClickListener(){
            viewModel.sendRegisterData()

        }
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it.valid)
                (activity as LoginActivity?)?.switchActivities()
        })
    }

}