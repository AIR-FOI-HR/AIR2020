package hr.foi.academiclifestyle.ui.auth

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
import hr.foi.academiclifestyle.ui.AuthActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider((activity as AuthActivity)).get(LoginViewModel::class.java)
    }
    private lateinit var loginBtn: Button
    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this

        val register = binding.registertextView
        loginBtn = binding.btnLogIn
        txtUsername = binding.txtUsernameInput
        txtPassword = binding.inputPasswordLogin

        //preserve previous field values
        binding.txtUsernameInput.setText(viewModel.usernameLogin.value)

        register.setOnClickListener(){
            (activity as AuthActivity?)?.switchFragment(1)
        }

        loginBtn.setOnClickListener(){
            toggleFields(false, "...", "grey")
            viewModel.sendLoginData()
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
        viewModel.responseLogin.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toggleFields(false, "✔", "green")
                if (it.valid)
                    (activity as AuthActivity?)?.switchActivities()
                viewModel.resetEvents(0)
            }
        })
        viewModel.responseType.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toggleFields(true, "Submit", "yellow")
                when (it) {
                    1 -> Toast.makeText(activity as AuthActivity?, "Username or password must not be empty!", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(activity as AuthActivity?, "Username and password don't match!", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(activity as AuthActivity?, "Server Error, please try again!", Toast.LENGTH_SHORT).show()
                }
                viewModel.resetEvents(1)
            }
        })
    }

    fun toggleFields(state: Boolean, text: String, color: String) {
        val colorRs = when (color) {
            "yellow" -> R.color.yellow_acc
            "green" -> R.color.green
            "grey" -> R.color.grey_30
            else -> R.color.yellow_acc
        }
        loginBtn.setBackgroundTintList(ContextCompat.getColorStateList((activity as AuthActivity), colorRs))
        loginBtn.setText(text)
        loginBtn.isEnabled = state
        loginBtn.isClickable = state

        //fields
        var fieldDrawable = when (state) {
            true -> R.drawable.edit_text
            else -> R.drawable.edit_text_disabled
        }
        txtPassword.isEnabled = state
        txtUsername.isEnabled = state
        txtPassword.setBackgroundResource(fieldDrawable)
        txtUsername.setBackgroundResource(fieldDrawable)
    }
}