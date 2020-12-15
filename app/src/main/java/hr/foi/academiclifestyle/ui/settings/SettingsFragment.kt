package hr.foi.academiclifestyle.ui.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentSettingsBinding


class SettingsFragment: Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }


    private val viewModel: SettingsViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, SettingsViewModel.Factory(activity.application)).get(SettingsViewModel::class.java)
    }

    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtStudy: EditText
    private lateinit var txtYearOfStudy: EditText
    private lateinit var btnUpdateUser : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater, R.layout.fragment_settings, container, false)


        binding.lifecycleOwner = this

        txtFirstName = binding.editTextTextPersonName
        txtLastName = binding.editTextTextPersonLastName
        txtUsername = binding.editTextTextUsername
        txtPassword = binding.editTextTextPasword
        txtStudy = binding.editTextStudy
        txtYearOfStudy = binding.editTextYearOfStudy
        btnUpdateUser = binding.btnSaveSettings


        //Update users on button event clikc
        btnUpdateUser.setOnClickListener(){
            viewModel.updateUser()
        }

        //Send data from text fields to SettingsViewModel
        txtFirstName.doAfterTextChanged {
            viewModel.setFirstName(binding.editTextTextPersonName.text)
        }

        txtLastName.doAfterTextChanged {
            viewModel.setLastName(binding.editTextTextPersonLastName.text)
        }

        txtPassword.doAfterTextChanged {
            viewModel.setPassword(binding.editTextTextPasword.text)
        }

        txtUsername.doAfterTextChanged {
            viewModel.setUserName(binding.editTextTextUsername.text)
        }

        txtStudy.doAfterTextChanged {
            viewModel.setUserName(binding.editTextStudy.text)
        }

        txtYearOfStudy.doAfterTextChanged {
            viewModel.setYearOfStudy(binding.editTextYearOfStudy.text)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
        setThemeOptions()

    }

    fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_cogwheel)
        toolbar?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.grey_60))
        (activity as MainActivity?)?.window?.setStatusBarColor(ContextCompat.getColor(activity as MainActivity, R.color.grey_60));
        navHeader?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.grey_60))

        setNavigationColors()
    }

    fun setNavigationColors() {
        val navView = (activity as MainActivity?)?.findViewById<NavigationView>(R.id.navView)
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf())

        val colors = intArrayOf(
            (activity as MainActivity).getColor(R.color.grey_80), //unchecked
            (activity as MainActivity).getColor(R.color.grey_60), //checked
            (activity as MainActivity).getColor(R.color.grey_80)) //default

        val navigationViewColorStateList = ColorStateList(states, colors)

        navView?.setItemTextColor(navigationViewColorStateList)
        navView?.setItemIconTintList(navigationViewColorStateList)
    }
}
