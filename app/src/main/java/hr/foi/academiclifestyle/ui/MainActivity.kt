package hr.foi.academiclifestyle.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.jakewharton.threetenabp.AndroidThreeTen
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.ActivityMainBinding
import hr.foi.academiclifestyle.dimens.CoursesEnum


open class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var nameAndSurname: TextView
    private lateinit var study: TextView
    private lateinit var yearOfStudy: TextView
    private lateinit var profilePicture : ImageView

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.Factory(application)).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize timezone
        AndroidThreeTen.init(this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        //set up the toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.mainNavHostFragment)

        //pass each item individually to app bar so that it's considered a top-level destination (no back button)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.homeFragment, R.id.ambienceFragment, R.id.myClassesFragment, R.id.myBehavioursFragment, R.id.fragmentSettings), drawerLayout)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

        //disable automatic icon tinting
        binding.navView.itemIconTintList = null

        //set the animated drawer toggle
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupObservers()

        //Set user information on menu
        val header: View = binding.navView.getHeaderView(0)
        nameAndSurname = (header.findViewById<View>(R.id.navUser) as TextView)
        study = (header.findViewById<View>(R.id.navStudy) as TextView)
        yearOfStudy = (header.findViewById<View>(R.id.navYear) as TextView)
        profilePicture = header.findViewById<View>(R.id.imageView) as ImageView

        binding.navView.menu.findItem(R.id.log_out).setOnMenuItemClickListener() {
            viewModel.logoutUser()
            false
        }
    }

    //replace up button with nav button
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //handle back key press when drawer is open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.userDeleted.observe(this, Observer {
            if (it != null && it) {
                viewModel.resetEvents(1)
                switchActivities(false, true)
            }
        })
        viewModel.user?.observe(this, Observer {
            if (it != null && it.jwtToken != "" && it.rememberMe && !viewModel.tokenChecked) {
                viewModel.checkTokenAndUpdateUser(it)
            }
        })
        viewModel.valid.observe(this, Observer {
            if (it != null && !it)
                switchActivities(true, false)
        })

        viewModel.user?.observe(this, Observer {
            if(it != null){
                if(it.name != null && it.surname != null){


                    nameAndSurname.text = it.name + " " + it.surname
                }

                if(it.program != null){
                    if(it.program == 1){

                        study.text = CoursesEnum.IPS.programName
                    }
                    else
                        study.text = CoursesEnum.IPI.programName

                }

                if(it.year != null){
                    yearOfStudy.text = it.year.toString()+ ". godina"
                }
                if( it.imageURL != null){
                    viewModel.loadImage(it)
                }
                if(it.imageURL == ""){
                    profilePicture.setImageResource(R.drawable.ic_person)
                }
            }

        })

        viewModel.bitmapImage?.observe(this, Observer{
              if(it != null)
                profilePicture.setImageBitmap(it)
        })
    }


    private fun switchActivities(showExpireToast: Boolean, preventSleep: Boolean) {
        val intent = Intent(this, AuthActivity::class.java)
        if (showExpireToast)
            intent.putExtra("ShowTokenExpToast","True")
        if (preventSleep) //prevents splash screen delay
            intent.putExtra("PreventSleep","True")
        startActivity(intent)
        finish()
    }

}