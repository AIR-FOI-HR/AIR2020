package hr.foi.academiclifestyle.auth

import android.os.Bundle
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
import hr.foi.academiclifestyle.AuthActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {


    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider((activity as AuthActivity)).get(RegisterViewModel::class.java)
    }
    private lateinit var registerBtn: Button
    private lateinit var txtUsername: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtName: EditText
    private lateinit var txtPassword: EditText

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater,R.layout.fragment_register,container,false)
        binding.lifecycleOwner= this
        registerBtn = binding.btnRegister
        txtUsername = binding.txtRegisterUsername
        txtName = binding.txtNameRegister
        txtEmail = binding.txtEmailRegister
        txtPassword = binding.txtPasswordRegister

        //preserve previous field values
        txtUsername.setText(viewModel.username.value)
        txtEmail.setText(viewModel.email.value)
        txtName.setText(viewModel.fulname.value)

        binding.txtRegisterLogin.setOnClickListener{
            (activity as AuthActivity?)?.switchFragment(0)
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
        registerBtn.setOnClickListener(){
            toggleFields(false, "...", "grey")
            viewModel.sendRegisterData()
        }
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        viewModel.responseRegister.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it)
                    (activity as AuthActivity?)?.switchFragment(0)
                viewModel.resetEvents(0)
            }
        })
        viewModel.responseType.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toggleFields(true, "Submit", "yellow")
                when (it) {
                    1 -> Toast.makeText(activity as AuthActivity?, "All fields must be filled in!", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(activity as AuthActivity?, "Email not in a valid format!", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(activity as AuthActivity?, "Server Error, please try again!", Toast.LENGTH_SHORT).show()
                    4 -> {
                        Toast.makeText(activity as AuthActivity?, "Successfully registered, you may log in!", Toast.LENGTH_SHORT).show()
                        viewModel.resetFields()
                    }
                    5 -> Toast.makeText(activity as AuthActivity?, "Username already taken",Toast.LENGTH_SHORT).show()
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
        registerBtn.setBackgroundTintList(ContextCompat.getColorStateList((activity as AuthActivity), colorRs))
        registerBtn.setText(text)
        registerBtn.isEnabled = state
        registerBtn.isClickable = state

        //fields
        var fieldColor = when (state) {
            true -> R.color.white_acc
            else -> R.color.grey_30
        }
        txtName.isEnabled = state
        txtUsername.isEnabled = state
        txtEmail.isEnabled = state
        txtPassword.isEnabled = state
        txtName.setBackgroundTintList(ContextCompat.getColorStateList((activity as AuthActivity), fieldColor))
        txtUsername.setBackgroundTintList(ContextCompat.getColorStateList((activity as AuthActivity), fieldColor))
        txtEmail.setBackgroundTintList(ContextCompat.getColorStateList((activity as AuthActivity), fieldColor))
        txtPassword.setBackgroundTintList(ContextCompat.getColorStateList((activity as AuthActivity), fieldColor))
    }
}