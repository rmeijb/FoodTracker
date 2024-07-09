package com.example.foodtracker

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.foodtracker.database.consume.ConsumeDao

class DetailActivity : AppCompatActivity() {
    companion object {
        const val PRODUCT = "stopName"
    }
    private lateinit var stopName: String
    private lateinit var unit: String
    private var productId: Int = 0
    //var date: EditText? = null
    lateinit var datePickerDialog: DatePickerDialog
    //var date: EditText? = null
    private lateinit var consumeDao: ConsumeDao

    //fun addConsume(consume: Consume) = consumeDao.insertConsume(consume)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        stopName = intent?.extras?.getString(DetailActivity.PRODUCT).toString()
        productId = intent?.extras?.getInt("productId")!!
        unit = intent?.extras?.getString("unit").toString()

        val productlabel = findViewById<TextView>(R.id.product_detail_textView)
        productlabel.text = stopName
        val unitlabel = findViewById<TextView>(R.id.product_unit_textView)
        unitlabel.text = unit
        val quantity = findViewById<EditText>(R.id.editTextNumber)
        // initiate the date picker and a button
        val date = findViewById<EditText>(R.id.date)
        //date.setEnabled(false);
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
        date.setText(
            mDay.toString() + "/"
                    + (mMonth + 1) + "/" + mYear
        )
        // perform click event on edit text
        date.setOnClickListener { // calender class's instance and get current date , month and year from calender
            // date picker dialog
            datePickerDialog = DatePickerDialog(
                this@DetailActivity,
                { view, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                    date.setText(
                        dayOfMonth.toString() + "/"
                                + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        val button: Button = findViewById(R.id.opslaan)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Code here executes on main thread after user presses button
                //fun consumeDao(): ConsumeDao {}
                //(activity?.application as FoodtrackerApplication).database.consumeDao()
                //consumeDao = DetailActivity.Companion.  ConsumeDao()
                //var consume: Consume = Consume(id = 0, productId,date.text as String,unit, quantity.text as Double)
                //addConsume(consume)
                finish()
            }
        })
        val cancelButton: Button = findViewById(R.id.cancel)
        cancelButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Code here executes on main thread after user presses button
                finish()
            }
        })
    }
}