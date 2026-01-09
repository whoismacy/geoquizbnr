package com.bignerdranch.android.geoquizbnr

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquizbnr.databinding.ActivityMainBinding
import kotlin.math.roundToInt

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val questionBank =
        listOf(
            Question(R.string.question_kenya, true),
            Question(R.string.question_australia, true),
            Question(R.string.question_asia, true),
            Question(R.string.question_americas, true),
            Question(R.string.question_mideast, false),
        )
    private var currentIndex = 0
    private var correct = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.previousButton.setOnClickListener {
            currentIndex =
                if (currentIndex == 0) {
                    questionBank.size - 1
                } else {
                    currentIndex - 1
                }
            updateQuestion()
        }
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val currentQuestion = questionBank[currentIndex]
        binding.questionTextView.setText(currentQuestion.textResId)
        val enabled = !currentQuestion.answered
        binding.trueButton.isEnabled = enabled
        binding.falseButton.isEnabled = enabled
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId =
            if (userAnswer == correctAnswer) {
                correct += 1
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        questionBank[currentIndex].answered = true
        val allAnswered: Boolean = questionBank.all { question: Question -> question.answered }
        if (allAnswered) {
            val percentage = (((correct * 1.0) / questionBank.size) * 100).roundToInt()
            Toast.makeText(this, "Score: $percentage%", Toast.LENGTH_LONG).show()
        }
    }
}
