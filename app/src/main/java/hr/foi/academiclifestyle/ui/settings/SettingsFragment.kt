package hr.foi.academiclifestyle.ui.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build.*
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentSettingsBinding
import hr.foi.academiclifestyle.ui.MainActivity
import java.io.File
import android.Manifest
import android.provider.MediaStore
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout


class SettingsFragment: Fragment(), AdapterView.OnItemSelectedListener {

    private var mediaPath: String? = null
    private val viewModel: SettingsViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, SettingsViewModel.Factory(activity.application)).get(
            SettingsViewModel::class.java
        )
    }

    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtUsername: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtYearOfStudy: EditText
    private lateinit var txtSemester: EditText
    private lateinit var btnUpdateUser : Button
    private lateinit var btnChoosePicture : Button
    private lateinit var program :Spinner
    private lateinit var image : ImageView
    private lateinit var imageFile : File
    private lateinit var setDefaultImage : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )

        binding.lifecycleOwner = this

        txtFirstName = binding.editTextTextPersonName
        txtLastName = binding.editTextTextPersonLastName
        txtUsername = binding.editTextTextUsername
        txtEmail = binding.editTextTextEmail
        txtYearOfStudy = binding.editTextYearOfStudy
        txtSemester = binding.editTextSemester
        btnUpdateUser = binding.btnSaveSettings
        btnChoosePicture = binding.btnSettingsChooseImage
        program = binding.spinner
        image = binding.imageView
        setDefaultImage = binding.btnSetDefaultImage

        //Create spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.programs_array,
            R.layout.spinner_programs
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_drop_down)
            // Apply the adapter to the spinner
            program.adapter = adapter

            program.onItemSelectedListener = this
        }

        //Update users on button event click
        btnUpdateUser.setOnClickListener(){
            toggleButtonState(false, "...", "grey")
            viewModel.updateUser()
        }

        //Send data from text fields to SettingsViewModel
        txtFirstName.doAfterTextChanged {
            viewModel.setFirstName(binding.editTextTextPersonName.text)
        }

        txtLastName.doAfterTextChanged {
            viewModel.setLastName(binding.editTextTextPersonLastName.text)
        }

        txtEmail.doAfterTextChanged {
            viewModel.setEmail(binding.editTextTextEmail.text)
        }

        txtUsername.doAfterTextChanged {
            viewModel.setUserName(binding.editTextTextUsername.text)
        }

        txtYearOfStudy.doAfterTextChanged {
            viewModel.setYearOfStudy(binding.editTextYearOfStudy.text)
        }

        txtSemester.doAfterTextChanged {
            viewModel.setSemester(binding.editTextSemester.text)
        }


        //Choose picture
        btnChoosePicture.setOnClickListener(){
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        setDefaultImage.setOnClickListener(){
            viewModel.resetImage()
            Toast.makeText(
                    activity as MainActivity?,
                    "Image reset!",
                    Toast.LENGTH_SHORT
            ).show()
        }


        // fix toggle animation for navView
        val drawerLayout : DrawerLayout = (activity as MainActivity).findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle((activity as MainActivity), (activity as MainActivity).findViewById(R.id.drawerLayout), R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        setupObservers()
        setThemeOptions()
        return binding.root
    }

    fun toggleButtonState(state: Boolean, text: String, color: String) {
        val colorRs = when (color) {
            "red" -> R.color.foi_red
            "grey" -> R.color.grey_30
            else -> R.color.foi_red
        }
        btnUpdateUser.setBackgroundTintList(
            ContextCompat.getColorStateList(
                (activity as MainActivity),
                colorRs
            )
        )
        btnUpdateUser.setText(text)
        btnUpdateUser.isEnabled = state
        btnUpdateUser.isClickable = state
    }

    private fun setupObservers(){
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.name != null) {
                    txtFirstName.text = Editable.Factory.getInstance().newEditable(it.name)
                }
                if (it.surname != null) {
                    txtLastName.text = Editable.Factory.getInstance().newEditable(it.surname)
                }

                if (it.username != null) {
                    txtUsername.text = Editable.Factory.getInstance().newEditable(it.username)
                }
                if (it.email != null) {
                    txtEmail.text = Editable.Factory.getInstance().newEditable(it.email)
                }

                if (it.program != null) {
                    if (it.program == 1) {
                        program.setSelection(0)
                    } else {
                        program.setSelection(1)
                    }
                }

                if (it.year != null) {
                    txtYearOfStudy.text =
                        Editable.Factory.getInstance().newEditable(it.year.toString())
                }

                if(it.semester != null){
                    txtSemester.text = Editable.Factory.getInstance().newEditable(it.semester.toString())
                }
            }


        })

        viewModel.responseType.observe(viewLifecycleOwner, Observer {
            toggleButtonState(true, getString(R.string.saveSettings), "red")
            when (it) {
                1 -> Toast.makeText(
                    activity as MainActivity?,
                    "Profile updated!",
                    Toast.LENGTH_SHORT
                ).show()
                2 -> Toast.makeText(
                    activity as MainActivity?,
                    "Bad request!",
                    Toast.LENGTH_SHORT
                ).show()
                3 -> Toast.makeText(
                    activity as MainActivity?,
                    "Server Error, please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_cogwheel)
        toolbar?.setBackgroundColor(
            ContextCompat.getColor(
                activity as MainActivity,
                R.color.grey_60
            )
        )
        (activity as MainActivity?)?.window?.setStatusBarColor(
            ContextCompat.getColor(
                activity as MainActivity,
                R.color.grey_60
            )
        );
        navHeader?.setBackgroundColor(
            ContextCompat.getColor(
                activity as MainActivity,
                R.color.grey_60
            )
        )

        setNavigationColors()
    }

    fun setNavigationColors() {
        val navView = (activity as MainActivity?)?.findViewById<NavigationView>(R.id.navView)
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked),
            intArrayOf()
        )

        val colors = intArrayOf(
            (activity as MainActivity).getColor(R.color.grey_80), //unchecked
            (activity as MainActivity).getColor(R.color.grey_60), //checked
            (activity as MainActivity).getColor(R.color.grey_80)
        ) //default

        val navigationViewColorStateList = ColorStateList(states, colors)

        navView?.setItemTextColor(navigationViewColorStateList)
        navView?.setItemIconTintList(navigationViewColorStateList)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()

        if(text == "Informacijski i poslovni sustavi"){
            viewModel.setStudy(1)
        }

        if(text == "Informacijsko i programsko inženjerstvo"){
            viewModel.setStudy(2)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        val text: String = parent.toString()

        if(text == "Informacijski i poslovni sustavi"){
            viewModel.setStudy(1)
        }

        if(text == "Informacijsko i programsko inženjerstvo"){
            viewModel.setStudy(2)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {

                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            if(data != null){
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context?.contentResolver?.query(selectedImage!!,filePathColumn,null,null,null)
                assert(cursor != null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                mediaPath =cursor.getString(columnIndex)
                cursor.close()
            }
            image.setImageURI(data?.data)
            //val uri : String = data?.dataString!!
            var file = File(data!!.data!!.path)
            imageFile = File(mediaPath)
            try {
                viewModel.setPicture(imageFile)
            }catch (ex: Exception){}
            image.visibility = View.VISIBLE
        }
    }

}
