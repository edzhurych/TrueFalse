package com.test.truefalse.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import com.test.truefalse.R
import com.test.truefalse.adapters.FactsRecyclerAdapter
import com.test.truefalse.constants.LIST_ANSWERS_KEY
import com.test.truefalse.databinding.FragmentResultBinding
import com.test.truefalse.model.Answer
import com.test.truefalse.view.BaseFragment
import com.test.truefalse.viewModel.ResultViewModel


class ResultFragment : BaseFragment<ResultViewModel, FragmentResultBinding>() {

    override fun layout() = R.layout.fragment_result

    override fun afterCreateView(view: View, savedInstanceState: Bundle?) {
        vb.vm = vm

        val recyclerView = vb.rvResultFragment

        val listAnswers = arguments?.getSerializable(LIST_ANSWERS_KEY) as ArrayList<Answer>
        Log.d("MyLogs", "ResultFragment. СПИСОК С ОТВЕТАМИ = ${listAnswers.size}")

        vm.calculatesCorrectAnswers(listAnswers)
        recyclerView.adapter = FactsRecyclerAdapter(listAnswers)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("MyLogs", "GameFragment. onOptionsItemSelected")
        when (item.itemId) {
            R.id.menuNewGame -> {
                findNavController().navigate(R.id.gameFragment)
                Log.d("MyLogs", "конец реализации кнопки Новая игра")
                return true
            }
            R.id.menuSetting -> {
                return true
            }
            R.id.menuAboutApp -> {
                return true
            }
            R.id.menuExit -> {
                activity?.finish()
                return true
            }
        }

        return false
    }
}