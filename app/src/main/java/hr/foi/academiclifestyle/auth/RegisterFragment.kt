package hr.foi.academiclifestyle.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.LoginActivity
import hr.foi.academiclifestyle.R


class RegisterFragment : Fragment() {
    fun RegisterFragment() {
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val root=inflater.inflate(R.layout.fragment_register,container,false)

        val login =root.findViewById<TextView>(R.id.txtRegister_login)

        login.setOnClickListener{
            (activity as LoginActivity?)?.switchFragment(0)
        }
        return root
    }

}