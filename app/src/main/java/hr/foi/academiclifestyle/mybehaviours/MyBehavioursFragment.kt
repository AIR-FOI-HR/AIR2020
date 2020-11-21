package hr.foi.academiclifestyle.mybehaviours

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentMybehavioursBinding

class MyBehavioursFragment : Fragment() {

    companion object {
        fun newInstance() = MyBehavioursFragment()
    }

    private lateinit var viewModel: MyBehavioursViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMybehavioursBinding>(inflater, R.layout.fragment_mybehaviours, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyBehavioursViewModel::class.java)
        // TODO: Use the ViewModel
        setThemeOptions()
    }

    fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_bicycle)
        toolbar?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary))
        (activity as MainActivity?)?.window?.setStatusBarColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary));
        navHeader?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary))

        setNavigationColors()
    }

    fun setNavigationColors() {
        val navView = (activity as MainActivity?)?.findViewById<NavigationView>(R.id.navView)
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf())

        val colors = intArrayOf(
                (activity as MainActivity).getColor(R.color.grey_80), //unchecked
                (activity as MainActivity).getColor(R.color.red_primary), //checked
                (activity as MainActivity).getColor(R.color.grey_80)) //default

        val navigationViewColorStateList = ColorStateList(states, colors)

        navView?.setItemTextColor(navigationViewColorStateList)
        navView?.setItemIconTintList(navigationViewColorStateList)
    }
}