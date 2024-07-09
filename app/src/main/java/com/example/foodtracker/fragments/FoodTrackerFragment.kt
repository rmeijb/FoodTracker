package com.example.foodtracker.fragments

import android.media.Image
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.foodtracker.R
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.viewmodels.FoodTrackerViewModel

class FoodTrackerFragment : Fragment() {

    companion object {
        fun newInstance() = FoodTrackerFragment()
    }

    private lateinit var viewModel: FoodTrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.foodtracker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageFood: ImageView = view.findViewById(R.id.eten_imageView)
        imageFood.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToCategoryFragment()
                    view.findNavController().navigate(action)
                }
        })
        val imageBuik: ImageView = view.findViewById(R.id.buik_imageView)
        imageBuik.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToComplaintFragment()
                    view.findNavController().navigate(action)
                }
        })
        val imageLogboekList: ImageView = view.findViewById(R.id.logboekList_imageView)
        imageLogboekList.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToFoodlogFragment()
                    view.findNavController().navigate(action)
                }
        })
        val imageLogboekCalendar: ImageView = view.findViewById(R.id.logboekCalendar_imageView)
        imageLogboekCalendar.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToCalendarFragment()
                    view.findNavController().navigate(action)
                }
            })
        val imageOntlasting: ImageView = view.findViewById(R.id.ontlasting_imageView)
        imageOntlasting.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToOntlastingFragment()
                    view.findNavController().navigate(action)
                }
            })
        val imagePeriod: ImageView = view.findViewById(R.id.period_imageView)
        imagePeriod.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToPeriodFragment()
                    view.findNavController().navigate(action)
                }
            })
        val imageChart: ImageView = view.findViewById(R.id.chart_imageView)
        imageChart.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val action = FoodTrackerFragmentDirections
                        .actionFoodtrackerFragmentToChartFragment()
                    view.findNavController().navigate(action)
                }
            })
        val imageExport: ImageView = view.findViewById(R.id.export_imageView)
        imageExport.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View?) {
                    //val action = FoodTrackerFragmentDirections
                    //    .actionFoodtrackerFragmentToChartFragment()
                    //view.findNavController().navigate()
                }
            })

    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(FoodTrackerViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}