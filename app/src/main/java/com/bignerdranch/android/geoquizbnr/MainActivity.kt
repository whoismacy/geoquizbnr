package com.bignerdranch.android.geoquizbnr

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquizbnr.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private var cheatLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            // handle the result
        }

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
//            startActivity(intent)
            cheatLauncher.launch(intent)
        }
        updateQuestion()
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
