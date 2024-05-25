package com.openkin.automaticwatering.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    activity: FragmentActivity,
    private val fragmentsList: List<Fragment>
): FragmentStateAdapter(activity) {

    override fun getItemCount() = fragmentsList.size

    override fun createFragment(position: Int) = fragmentsList[position]
}