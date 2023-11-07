package com.waff1e.waffle.utils

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

interface MultipleClickDefender {
    fun processEvent(event: () -> Unit)

    companion object
}

fun MultipleClickDefender.Companion.get(): MultipleClickDefender = MultipleClickDefenderDefault()

class MultipleClickDefenderDefault: MultipleClickDefender {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs = 0L

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 500L) event.invoke()

        lastEventTimeMs = now
    }
}

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    val multipleClickDefender = remember { MultipleClickDefender.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleClickDefender.processEvent { onClick() } },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}