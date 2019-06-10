package com.github.farzadfarazmand.remindme.utils

import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.github.farzadfarazmand.remindme.R

@BindingAdapter("changeCardBackground")
fun cardViewBackground(cardView: CardView, color: Int) {
    try {
        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, color))
    } catch (e: Exception) {
        cardView.setCardBackgroundColor(color)
    }
}