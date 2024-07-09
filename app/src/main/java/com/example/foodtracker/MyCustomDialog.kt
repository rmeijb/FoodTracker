package com.example.foodtracker

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.database.foodlog.FoodlogDao
import com.example.foodtracker.database.foodlog.Type
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.database.ontlasting.OntlastingDao
import com.example.foodtracker.database.period.Period
import com.example.foodtracker.database.period.PeriodDao
import com.example.foodtracker.databinding.CustomDialogFragmentBinding
import com.example.foodtracker.viewmodels.FoodlogViewModel
import com.example.foodtracker.viewmodels.FoodlogViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MyCustomDialog: DialogFragment() {
    private var _binding: CustomDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var date: String

    private lateinit var period: Period
    private lateinit var periodDao: PeriodDao
    private lateinit var consumeDao: ConsumeDao
    private lateinit var ontlastingDao: OntlastingDao
    private lateinit var complaint: Complaint
    private lateinit var complaintDao: ComplaintDao
    private lateinit var foodlogDao : FoodlogDao
    private lateinit var repository: FoodlogRepository
    private lateinit var recyclerView: RecyclerView


    private val viewModel: FoodlogViewModel by activityViewModels {
        consumeDao = (activity?.application as FoodtrackerApplication).database.consumeDao()
        complaintDao = (activity?.application as FoodtrackerApplication).database.complaintDao()
        ontlastingDao = (activity?.application as FoodtrackerApplication).database.ontlastingDao()
        periodDao = (activity?.application as FoodtrackerApplication).database.periodDao()
        foodlogDao = (activity?.application as FoodtrackerApplication).database.foodlogDao()
        repository = FoodlogRepository(foodlogDao)
        FoodlogViewModelFactory(repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        getDialog()!!.getWindow()?.setBackgroundDrawable(null);
        _binding = CustomDialogFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val foodlogAdapter = FoodlogAdapter {
//            if (it.type == Type.Consume) {
//                val action = MyCustomDialogDirections
//                    .actionCalendarFragmentToConsumeUpdateFragment(
//                        consume = consumeDao.getConsume(it.id)
//                    )
//                view.findNavController().navigate(action)
//            } else if (it.type == Type.Complaint) {
//                val action = MyCustomDialogDirections
//                    .actionCalendarFragmentToComplaintUpdateFragment(
//                        complaint = complaintDao.getComplaint(it.id)
//                    )
//                view.findNavController().navigate(action)
//            } else if (it.type == Type.Ontlasting) {
//                val action = com.example.foodtracker.fragments.MyCustomDialogDirections
//                    .actionCalendarFragmentToOntlastingUpdateFragment(
//                        ontlasting = ontlastingDao.getOntlasting(it.id)
//                    )
//                view.findNavController().navigate(action)
//            } else if (it.type == Type.Period) {
//                val action = com.example.foodtracker.fragments.MyCustomDialogDirections
//                    .actionCalendarFragmentToPeriodUpdateFragment(
//                        period = periodDao.getPeriod(it.id)
//                    )
//                view.findNavController().navigate(action)
//            }
        }
        recyclerView.adapter = foodlogAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fullConsume().collect {
                    val filteredlist = it.filter { dateFilter(it.id, it.type, it.startdate, date) }
                    binding.tvDate.text = date.toString()
                    foodlogAdapter.submitList(filteredlist)
                }
            }
        }
        val cancelButton: Button = view.findViewById(R.id.btn_cancel)
        cancelButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                getDialog()!!.cancel()
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateFilter(id : Int, type : Type, date : String, selectedDate: String): Boolean {
        if (type == Type.Period) {
            period = periodDao.getPeriod(id)
            return (LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("d/M/yyyy")) >= LocalDate.parse(period.startdatetime,DateTimeFormatter.ofPattern("d/M/yyyy")) && LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("d/M/yyyy")) <= LocalDate.parse(period.enddatetime,DateTimeFormatter.ofPattern("d/M/yyyy")))
        } else if (type == Type.Complaint) {
            complaint = complaintDao.getComplaint(id)
            return (LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("d/M/yyyy")) >= LocalDate.parse(complaint.startdatetime,DateTimeFormatter.ofPattern("d/M/yyyy")) && LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("d/M/yyyy")) <= LocalDate.parse(complaint.enddatetime,DateTimeFormatter.ofPattern("d/M/yyyy")))
        }
        return (LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) == LocalDate.parse(selectedDate,DateTimeFormatter.ofPattern("d/M/yyyy")))
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val formatter = DateTimeFormatter.ofPattern("d/M/uuuu")
            val localDate = LocalDate.parse(it.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            date = localDate.format(formatter)
        }
    }

}