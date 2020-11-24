package hr.foi.academiclifestyle.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hr.foi.academiclifestyle.LoginActivity
import hr.foi.academiclifestyle.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this

        val register = binding.registertextView
        val loginSubmit = binding.btnLogIn

        register.setOnClickListener(){
            (activity as LoginActivity?)?.switchFragment(1)
        }

        loginSubmit.setOnClickListener(){
            toggleBtn(binding.btnLogIn, false)
            viewModel.sendLoginData()
            toggleBtn(binding.btnLogIn, true)
        }

        binding.txtUsernameInput.doAfterTextChanged {
            viewModel.setUsername(binding.txtUsernameInput.text)
        }

        binding.inputPasswordLogin.doAfterTextChanged {
            viewModel.setPassword(binding.inputPasswordLogin.text)
        }

        setupObservers()
        return binding.root
    }

    private fun setupObservers(){
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it.valid)
                (activity as LoginActivity?)?.switchActivities()
        })
        viewModel.responseType.observe(viewLifecycleOwner, Observer {
            when (it) {
                1 -> Toast.makeText(activity as LoginActivity?, "Username or password must not be empty!", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(activity as LoginActivity?, "Server Error, please try again!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleBtn(button: Button, enabled: Boolean) {
        if (enabled) {
            button.isEnabled = true
            button.isClickable = true
        } else {
            button.isEnabled = false
            button.isClickable = false
        }
    }
}