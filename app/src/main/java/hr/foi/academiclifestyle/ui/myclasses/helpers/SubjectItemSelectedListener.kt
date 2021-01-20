package hr.foi.academiclifestyle.ui.myclasses.helpers

import android.view.View
import android.widget.AdapterView
import hr.foi.academiclifestyle.ui.myclasses.StatsGoalsViewModel
import kotlin.reflect.KFunction0

class SubjectItemSelectedListener(viewModel: StatsGoalsViewModel, startAnimFunc: KFunction0<Unit>): AdapterView.OnItemSelectedListener {
    val vModel: StatsGoalsViewModel = viewModel
    val startAnimation = startAnimFunc
    var counter: Int = 0

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //skip first trigger on inital load
        if (counter > 0) {
            startAnimation()
        } else {
            counter += 1
        }
        val selectedItem = parent?.getItemAtPosition(position) as String
        vModel.getGraphData(selectedItem)
    }
}