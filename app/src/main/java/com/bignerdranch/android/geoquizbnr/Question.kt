package com.bignerdranch.android.geoquizbnr

import androidx.annotation.StringRes


data class Question(@StringRes val textResId: Int, val answer: Boolean)