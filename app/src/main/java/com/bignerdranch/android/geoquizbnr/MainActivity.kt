package com.bignerdranch.android.geoquizbnr

import android.app.Activity
import android.os.Build
import android.os.Bundle
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
            if (result.resultCode == Activity.RESULT_OK) {
                val hasCheated = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                val returnedCheats = result.data?.getIntExtra(EXTRA_CURRENT_CHEAT_COUNT, 0) ?: 0
                quizViewModel.currentQuestion.cheated = hasCheated
                quizViewModel.currentQuestion.answered = true
                quizViewModel.isCheater = hasCheated
                quizViewModel.cheatCount += returnedCheats
                updateButtonState()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val androidRelease = Build.VERSION.RELEASE ?: "Unknown"
        val sdkVersion = Build.VERSION.SDK_INT_FULL
        binding.androidVersion.text = "You are currently using Android: $androidRelease SDK Version $sdkVersion"

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
            if (quizViewModel.cheatCount >= 3) {
                updateButtonState()
                return@setOnClickListener
            }
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
        updateButtonState()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (quizViewModel.currentQuestionCheatState) {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show()
            updateButtonState()
        } else {
            val correctAnswer = quizViewModel.currentQuestionAnswer
            val messageResId =
                when {
                    quizViewModel.isCheater -> {
                        R.string.judgment_toast
                    }

                    userAnswer == correctAnswer -> {
                        quizViewModel.correct += 1
                        R.string.correct_toast
                    }

                    else -> {
                        R.string.incorrect_toast
                    }
                }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
            quizViewModel.isCheater = if (quizViewModel.isCheater) false else quizViewModel.isCheater
            quizViewModel.currentQuestion.answered = true
            if (quizViewModel.checkAllAnswered()) {
                Toast.makeText(this, "Score: ${quizViewModel.percentage}%", Toast.LENGTH_LONG).show()
            }
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        val enabledAnswered = !quizViewModel.currentQuestion.answered
        val enabledCheated = !quizViewModel.currentQuestion.cheated

        binding.trueButton.isEnabled = enabledCheated && enabledAnswered
        binding.falseButton.isEnabled = enabledCheated && enabledAnswered

        if (quizViewModel.cheatCount >= 3 || quizViewModel.checkAllAnswered()) {
            binding.cheatButton.isEnabled = false
            Toast.makeText(this, R.string.cheating_cancel, Toast.LENGTH_SHORT).show()
        }
    }
}
