package com.cusufcan.besinkitabi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cusufcan.besinkitabi.databinding.FoodItemBinding
import com.cusufcan.besinkitabi.model.Food
import com.cusufcan.besinkitabi.util.createPlaceHolder
import com.cusufcan.besinkitabi.util.downloadFromUrl
import com.cusufcan.besinkitabi.view.fragment.FoodListFragmentDirections

class FoodAdapter(private val foodList: ArrayList<Food>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    inner class FoodViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.FoodViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodAdapter.FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.binding.foodName.text = food.name
        holder.binding.foodCalorie.text = food.calorie

        holder.itemView.setOnClickListener {
            val action =
                FoodListFragmentDirections.actionFoodListFragmentToFoodDetailFragment(food.uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadFromUrl(
            food.image,
            createPlaceHolder(holder.itemView.context),
        )
    }

    fun updateFoodList(newFoodList: List<Food>) {
        foodList.clear()
        foodList.addAll(newFoodList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}