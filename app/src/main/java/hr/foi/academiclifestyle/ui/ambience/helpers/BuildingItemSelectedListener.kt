package hr.foi.academiclifestyle.ui.ambience.helpers

import android.view.View
import android.widget.AdapterView
import hr.foi.academiclifestyle.ui.ambience.OtherRoomsViewModel
import kotlin.reflect.KFunction0

class BuildingItemSelectedListener(viewModel: OtherRoomsViewModel, startAnimFunc: KFunction0<Unit>): AdapterView.OnItemSelectedListener {
    val vModel: OtherRoomsViewModel = viewModel
    val startAnimation = startAnimFunc
    var counter: Int = 0

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //skip first trigger on inital load
        if (counter > 0) {
            startAnimation()
            vModel.fetchRoomList(parent?.getItemAtPosition(position) as String)
        } else {
            counter += 1
        }
    }
}