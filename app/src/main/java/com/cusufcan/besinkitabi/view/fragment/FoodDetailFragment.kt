package com.cusufcan.besinkitabi.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cusufcan.besinkitabi.databinding.FragmentFoodDetailBinding
import com.cusufcan.besinkitabi.util.createPlaceHolder
import com.cusufcan.besinkitabi.util.downloadFromUrl
import com.cusufcan.besinkitabi.viewmodel.FoodDetailViewModel

class FoodDetailFragment : Fragment() {
    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FoodDetailViewModel

    var foodId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodDetailViewModel::class.java]

        arguments?.let {
            foodId = FoodDetailFragmentArgs.fromBundle(it).foodId
        }

        viewModel.fetchFromRoom(foodId)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.foodLiveData.observe(viewLifecycleOwner) {
            binding.foodName.text = it.name
            binding.foodCalorie.text = it.calorie
            binding.foodCarb.text = it.carb
            binding.foodProtein.text = it.protein
            binding.foodFat.text = it.fat
            binding.foodImage.downloadFromUrl(it.image, createPlaceHolder(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}