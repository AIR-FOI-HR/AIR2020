package hr.foi.academiclifestyle.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.dimens.ScheduleEvent

class CustomDialog {
    fun showDialog(context: Context, event: ScheduleEvent) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog)

        dialog.findViewById<TextView>(R.id.txtDTitle).text = event.name
        val dialogButton: Button = dialog.findViewById(R.id.btnDConfirm)

        if (event.status == 1) {
            dialog.findViewById<TextView>(R.id.txtDInfo).visibility = View.INVISIBLE
            dialogButton.text = "Close"
            dialogButton.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_60))
            dialog.findViewById<TextView>(R.id.txtDPresenceVar).text = event.userLogTime
        }

        dialogButton.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.show()

        // Fix Dialog width
        val window: Window = dialog.window!!
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        // Fix background
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}