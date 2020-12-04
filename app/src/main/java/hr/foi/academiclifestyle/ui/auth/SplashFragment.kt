package hr.foi.academiclifestyle.ui.auth

import android.os.Bundle
import android.util.Log
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
import hr.foi.academiclifestyle.ui.AuthActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentLoginBinding
import hr.foi.academiclifestyle.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay


class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, SplashViewModel.Factory(activity.application)).get(SplashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSplashBinding>(inflater, R.layout.fragment_splash, container, false)
        binding.lifecycleOwner = this

        setupObservers()
        return binding.root
    }

    private fun setupObservers(){
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            val doSleep = (activity as AuthActivity).intent.getStringExtra("PreventSleep") == "True"
            if (!doSleep)
                Thread.sleep(500) //this is the cause of skipped frames!
            if (it != null && it.jwtToken != "") {
                (activity as AuthActivity?)?.switchActivities()
            } else {
                (activity as AuthActivity?)?.switchFragment(0)
            }
        })
    }
}