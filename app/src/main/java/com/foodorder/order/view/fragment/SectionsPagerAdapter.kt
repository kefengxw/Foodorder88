package com.foodorder.order.view.fragment;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.foodorder.order.model.data.DeviceRole
import com.foodorder.order.model.data.InternalDataConfiguration.DEVICE_CURRENT_ROLE
import com.foodorder.order.model.data.InternalDataConfiguration.getTotalTabNumber
import com.foodorder.order.model.data.SectionFragmentChef.*
import com.foodorder.order.model.data.SectionFragmentGuest.*
import com.foodorder.order.model.data.SectionFragmentManager.SECTION_SUMMARY_DAY1_FRAG1
import com.foodorder.order.model.data.SectionFragmentManager.SECTION_SUMMARY_DAY2_FRAG2
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SectionsPagerAdapter(fm: FragmentManager, val mFabtn: FloatingActionButton) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) { //FragmentPagerAdapter(fm)

    override fun getCount(): Int {
        return getTotalTabNumber()//Can be adjust by customer
    }

    override fun getItem(position: Int): Fragment {
        return getBaseFragment(position)
    }

    private fun getBaseFragment(position: Int): BaseFragment {
        return when (DEVICE_CURRENT_ROLE) {
            DeviceRole.Manager -> {
                getManagerBaseFragment(position)
            }
            DeviceRole.Chef -> {
                getChefBaseFragment(position)
            }
            else -> /*Guest*/ {
                getGuestBaseFragment(position)
            }
        }
    }

    private fun getManagerBaseFragment(position: Int): BaseFragment {
        return when (position) {
            SECTION_SUMMARY_DAY1_FRAG1.ordinal -> {
                ManagerFrag.newInstance(SECTION_SUMMARY_DAY1_FRAG1)
            }
            else -> {//SECTION_SUMMARY_DAY2_FRAG2
                ManagerFrag.newInstance(SECTION_SUMMARY_DAY2_FRAG2)
            }
        }
    }

    private fun getChefBaseFragment(position: Int): BaseFragment {
        return when (position) {
            SECTION_COOKING_DAY1_FRAG1.ordinal -> {
                ChefFrag.newInstance(SECTION_COOKING_DAY1_FRAG1)
            }
            SECTION_COOKING_DAY2_FRAG2.ordinal -> {
                ChefFrag.newInstance(SECTION_COOKING_DAY2_FRAG2)
            }
            else -> {//SECTION_COOKING_DAY3_FRAG3
                ChefFrag.newInstance(SECTION_COOKING_DAY3_FRAG3)//case SECTION_COOKING_DAY3_FRAG3:
            }
        }
    }

    private fun getGuestBaseFragment(position: Int): BaseFragment {
        return when (position) {
            SECTION_WELCOME_FRAG1.ordinal -> {
                WelcomeFrag.newInstance(SECTION_WELCOME_FRAG1)
            }
            SECTION_SPECIAL_FRAG2.ordinal -> {
                SpecialFrag.newInstance(SECTION_SPECIAL_FRAG2)
            }
            SECTION_STARTER_FRAG3.ordinal -> {
                StarterFrag.newInstance(SECTION_STARTER_FRAG3)
            }
            SECTION_MAIN_FRAG4.ordinal -> {
                MainCourseFrag.newInstance(SECTION_MAIN_FRAG4)
            }
            SECTION_DESSERT_FRAG5.ordinal -> {
                DessertFrag.newInstance(SECTION_DESSERT_FRAG5)
            }
            SECTION_WINE_FRAG6.ordinal -> {
                WineDrinkFrag.newInstance(SECTION_WINE_FRAG6)
            }
            else -> {//case SECTION_CONCEPT_FRAG7:
                ConceptFrag.newInstance(SECTION_CONCEPT_FRAG7)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return getBasePageTitle(position)
    }

    private fun getBasePageTitle(position: Int): CharSequence {
        return when (DEVICE_CURRENT_ROLE) {
            DeviceRole.Manager -> {
                getManagerBasePageTitle(position)
            }
            DeviceRole.Chef -> {
                getChefBasePageTitle(position)
            }
            else -> /*Guest*/ {
                getGuestBasePageTitle(position)
            }
        }
    }

    private fun getManagerBasePageTitle(position: Int): CharSequence {

        val it = arrayOf(
            "SummaryDay1",
            "SummaryDay2"
        )

        if (position >= it.size) return ""
        return it[position]
    }

    private fun getChefBasePageTitle(position: Int): CharSequence {

        val it = arrayOf(
            "CookingDay1",
            "CookingDay2",
            "CookingDay3"
        )

        if (position >= it.size) return ""
        return it[position]
    }

    private fun getGuestBasePageTitle(position: Int): CharSequence {

        val it = arrayOf(
            "Welcome",
            "Special",
            "Starter",
            "Main",
            "Dessert",
            "Wine&Drink",
            "Concept"
        )

        if (position >= it.size) return ""
        return it[position]
    }
}