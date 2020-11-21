package hr.foi.academiclifestyle.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.LoginActivity
import hr.foi.academiclifestyle.MainActivity
import hr.foi.academiclifestyle.R


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root=inflater.inflate(R.layout.fragment_login, container, false)

        val register =root.findViewById<TextView>(R.id.registertextView)
        val loginSubmit=root.findViewById<View>(R.id.btnLogIn)

        register.setOnClickListener(){
            (activity as LoginActivity?)?.switchFragment(1)

        }

        loginSubmit.setOnClickListener(){
            (activity as LoginActivity?)?.switchActivities()

        }
        return root
    }







}