package hr.foi.academiclifestyle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.auth.LoginFragment
import hr.foi.academiclifestyle.auth.RegisterFragment


class LoginActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        var fragment = LoginFragment()
        val transaction= supportFragmentManager.beginTransaction()
        transaction.replace(R.id.loginContainer, fragment, "LoginContainer")
        transaction.commit()

        val btnLogin= findViewById<View>(R.id.btnLogIn)
        val btnRegister=findViewById<Button>(R.id.btnRegister)

    }

    fun switchFragment(type: Int){
        lateinit var fragment:Fragment
        if(type == 1)
             fragment = RegisterFragment()
        else
             fragment = LoginFragment()
        val fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginContainer, fragment, "LoginContainer");
        fragmentTransaction.remove(supportFragmentManager.findFragmentByTag("LoginContainer")!!)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun switchActivities(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}