package com.bignerdranch.android.geoquizbnr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.UiContext
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquizbnr.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquizbnr.answer_is_true"

class MainActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.nextQuestion()
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener {
            quizViewModel.nextQuestion()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener {
            quizViewModel.previousQuestion()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivity(intent)
        }
        updateQuestion()
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            answerIsTrue: Boolean,
        ): Intent =
            Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
    }

    private fun updateQuestion() {
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
        val enabled = !quizViewModel.currentQuestion.answered
        binding.trueButton.isEnabled = enabled
        binding.falseButton.isEnabled = enabled
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId =
            if (userAnswer == correctAnswer) {
                quizViewModel.increaseCorrectCount()
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        quizViewModel.currentQuestion.answered = true
        if (quizViewModel.checkAllAnswered()) {
            Toast.makeText(this, "Score: ${quizViewModel.percentage}%", Toast.LENGTH_LONG).show()
        }
    }
}
