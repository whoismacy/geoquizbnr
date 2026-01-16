package com.bignerdranch.android.geoquizbnr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquizbnr.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquizbnr.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquizbnr.answer_shown"
const val EXTRA_CURRENT_CHEAT_COUNT = "con.bignerdranch.android.geoquizbnr.cheat_count"

class CheatActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    private var cheatCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        binding.showAnswerButton.setOnClickListener {
            cheatCount += 1
            val answerText =
                when {
                    answerIsTrue -> R.string.true_button
                    else -> R.string.false_button
                }
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(true, cheatCount)
        }
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

    private fun setAnswerShownResult(
        isAnswerShown: Boolean,
        cheatCount: Int,
    ) {
        val data =
            Intent().apply {
                putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
                putExtra(EXTRA_CURRENT_CHEAT_COUNT, cheatCount)
            }
        setResult(Activity.RESULT_OK, data)
    }
}
