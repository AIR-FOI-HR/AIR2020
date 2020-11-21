package hr.foi.academiclifestyle.loginAndRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.LoginActivity
import hr.foi.academiclifestyle.R
import org.w3c.dom.Text


class RegisterFragment : Fragment() {
    fun RegisterFragment() {
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val root=inflater.inflate(R.layout.fragment_register,container,false)

        val login =root.findViewById<TextView>(R.id.txtRegister_login)

        login.setOnClickListener{
            (activity as LoginActivity?)?.switchFragment(0)
        }
        return root
    }

}