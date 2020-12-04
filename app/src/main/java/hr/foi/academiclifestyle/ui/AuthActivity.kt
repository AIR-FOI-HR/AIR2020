package hr.foi.academiclifestyle.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.ui.auth.LoginFragment
import hr.foi.academiclifestyle.ui.auth.RegisterFragment
import hr.foi.academiclifestyle.ui.auth.SplashFragment


class AuthActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var fragment = SplashFragment()
        val transaction= supportFragmentManager.beginTransaction()
        transaction.replace(R.id.loginContainer, fragment, "LoginContainer")
        transaction.commit()
    }

    fun switchFragment(type: Int) {
        lateinit var fragment:Fragment
        if (type == 1)
             fragment = RegisterFragment()
        else
             fragment = LoginFragment()
        val fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginContainer, fragment, "LoginContainer")
        fragmentTransaction.commit()
    }

    fun switchActivities() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}