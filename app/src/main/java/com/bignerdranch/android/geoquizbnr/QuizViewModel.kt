package com.bignerdranch.android.geoquizbnr

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "Viewmodel instance about to be destroyed")
    }

    private val questionBank =
        listOf(
            Question(R.string.question_kenya, true),
            Question(R.string.question_australia, true),
            Question(R.string.question_asia, true),
            Question(R.string.question_americas, true),
            Question(R.string.question_mideast, false),
        )

//    private var currentIndex: Int = 0
//        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
//        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    private var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    private var correct = 0

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestion: Question
        get() = questionBank[currentIndex]

    val percentage: Int
        get() = (((correct * 1.0) / questionBank.size) * 100).roundToInt()

    fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun previousQuestion() {
        currentIndex =
            if (currentIndex == 0) {
                questionBank.size - 1
            } else {
                currentIndex - 1
            }
    }

    fun increaseCorrectCount() {
        correct += 1
    }

    fun checkAllAnswered(): Boolean = questionBank.all { question -> question.answered }
}
