package com.test.truefalse.viewModel

import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import com.test.truefalse.R
import com.test.truefalse.constants.HARD
import com.test.truefalse.constants.LIST_ANSWERS_KEY
import com.test.truefalse.customClasses.CountDownTimer
import com.test.truefalse.database.Fact
import com.test.truefalse.model.Answer
import com.test.truefalse.model.repository.FactsRepositoryImpl
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GameViewModel
@Inject constructor(
    private val repository: FactsRepositoryImpl
) : ViewModel() {

    @Inject
    lateinit var idUnusedFacts: ArrayDeque<Int>
    @Inject
    lateinit var numberOfFactsAnswered: ObservableInt
    @Inject
    lateinit var currentFact: ObservableField<Fact>
    @Inject
    lateinit var isPlaying: ObservableBoolean
    @Inject
    lateinit var secondsLeft: ObservableInt
    @Inject
    lateinit var gameResult: ArrayList<Answer>

    var isPause: Boolean = false

    val liveFacts = liveData {
        emit(repository.getFacts())
    }

    // The first parameter is the number of milliseconds from which the count will be made
    // The second parameter is the number of milliseconds that are the callback interval (onTick () method)
    val countDownTimer = object : CountDownTimer(HARD, 1_000) {
        // Called when the timer reaches zero
        override fun onFinish() {
            // TODO: РЕАЛИЗОВАТЬ ЛОГИКУ ПРОИГРЫША
            Log.d("MyLogs", "MainViewModel. CountDownTimer. onFinish")
        }

        override fun onTick(millisUntilFinished: Long) {
            secondsLeft.set((millisUntilFinished / 1000).toInt())
            Log.d("MyLogs", "MainViewModel. CountDownTimer. onTick")
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun onButtonTrueClick(view: View) {
        countDownTimer.cancel()
        val oldValue = numberOfFactsAnswered.get()
        if (oldValue <= 28) {
            val isExistFacts = idUnusedFacts.isNotEmpty()
            if (isExistFacts) {
                isPlaying.set(true)
                countDownTimer.start()
                numberOfFactsAnswered.set(oldValue + 1)
                putGameResult(view)
                currentFact.set(liveFacts.value?.get(idUnusedFacts.pollLast()))
            } else {
                TODO("РЕАЛИЗОВАТЬ ЛОГИКУ, КОГДА ЗАКОНЧИЛИСЬ ФАКТЫ")
            }
        } else {
            isPlaying.set(false)
            putGameResult(view)
            val bundle = Bundle()
            bundle.putSerializable(LIST_ANSWERS_KEY, gameResult)
            view.findNavController().navigate(R.id.resultFragment, bundle)
        }
        Log.d("MyLogs", "MainViewModel. onButtonTrueClick")
        Log.d("MyLogs", "GameFragment. количество неиспользованных фактов = ${idUnusedFacts.size}")
    }

    private fun putGameResult(view: View) {
        val currentFact = currentFact.get() ?: return

        val answerResult: Boolean = when (view.id) {
            R.id.buttonGameFragmentTrue -> {
                true
            }
            else -> false
        }

        val answer = Answer(currentFact.name, currentFact.isTrue, answerResult)
        gameResult.add(answer)
    }
}