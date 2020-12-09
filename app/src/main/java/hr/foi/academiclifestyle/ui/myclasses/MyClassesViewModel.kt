package hr.foi.academiclifestyle.ui.myclasses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyClassesViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyClassesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyClassesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}