package me.varoa.studentprofiles.screen.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.varoa.studentprofiles.screen.detail.basic.BasicDetailFragment
import me.varoa.studentprofiles.screen.detail.profile.ProfileDetailFragment

class DetailStateAdapter(
    fragment: Fragment,
    private val studentId: Int,
) : FragmentStateAdapter(fragment) {
    companion object {
        const val ARGS_ID = "id"
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BasicDetailFragment()
            else -> ProfileDetailFragment()
        }.apply {
            arguments = Bundle().apply { putInt(ARGS_ID, studentId) }
        }
    }
}
