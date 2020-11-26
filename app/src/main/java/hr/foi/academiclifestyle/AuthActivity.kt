package hr.foi.academiclifestyle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.auth.LoginFragment
import hr.foi.academiclifestyle.auth.RegisterFragment


class AuthActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var fragment = LoginFragment()
        val transaction= supportFragmentManager.beginTransaction()
        transaction.replace(R.id.loginContainer, fragment, "LoginContainer")
        transaction.commit()
    }

    fun switchFragment(type: Int){
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