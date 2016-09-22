package com.aziis98.utils

import com.sun.scenario.effect.Merge
import java.util.function.Predicate

/**
 * Created by aziis98 on 19/09/2016.
 */

/*
[";test; ;prova;";\n;altro; ;test;";test;\";boh;\";"]
fn: MergeState.(element: T) -> MergeState
fn: (list: List<T>) -> T
 */

enum class MergeState {
    None, Open, Continue, Close
}

data class MatchPreset<T>(val type: String, val start: (T) -> Boolean, val end: (T) -> Boolean)

data class MergeData(val state: MergeState, val type: String) {
    companion object {
        val None = MergeData(MergeState.None, "none")
    }
}

fun <T> testOf(value: T) = { it : T -> it == value }

internal fun <T> createMergePredicate(predicate: List<MatchPreset<T>>): (MergeData, T) -> MergeData {
    return { data, element ->
        val foundStart = predicate.find { it.start(element) }
        val foundEnd = predicate.find { data.type == it.type && it.end(element) }
        when {
            data.state == MergeState.None
                    && foundStart != null
                -> MergeData(MergeState.Open, foundStart.type)
            data.state == MergeState.Continue
                    && foundEnd != null
                -> data.copy(state = MergeState.Close)
            else -> data
        }
    }
}

fun <T> templateOf(type: String, start: T, end: T)
        = templateOf(type, testOf(start), testOf(end))

fun <T> templateOf(type: String, start: (T) -> Boolean, end: (T) -> Boolean)
        = MatchPreset(type, start, end)

fun <T> templatesOf(vararg triggers: MatchPreset<T>)
        : (MergeData, T) -> MergeData {
    return createMergePredicate(triggers.toList())
}

inline fun <T> List<T>.merge(combiner: (list: List<T>) -> T,
                             mergePredicate: MergeData.(element: T) -> MergeData): List<T> {
    val newlist = mutableListOf<T>()
    val buffer = mutableListOf<T>()

    var mergeData = MergeData.None

    forEach {
        mergeData = mergeData.mergePredicate(it)
        when (mergeData.state) {
            MergeState.Open -> {
                mergeData = mergeData.copy(state = MergeState.Continue)
                buffer.clear()
                buffer.add(it)
            }
            MergeState.Continue -> {
                buffer.add(it)
            }
            MergeState.Close -> {
                mergeData = MergeData.None
                buffer.add(it)
                newlist.add(combiner(buffer))
            }
            MergeState.None -> {
                newlist.add(it)
            }
        }
    }

    return newlist
}