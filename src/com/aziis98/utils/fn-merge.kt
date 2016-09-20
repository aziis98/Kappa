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

fun <T> testOf(value: T) = { it : T -> it == value }

internal fun <T> preset(predicate: Pair<(T) -> Boolean, (T) -> Boolean>): (MergeState, T) -> MergeState {
    return { state, element ->
        when {
            state == MergeState.None && predicate.first(element) -> MergeState.Open
            state == MergeState.Continue && predicate.second(element) -> MergeState.Close

            else -> state
        }
    }
}

fun <T> presetsOf(vararg triggers: Pair<(T) -> Boolean, (T) -> Boolean>)
        : (MergeState, T) -> MergeState {
    val unzipped = triggers.unzip()

    return preset(Pair(
            { it -> unzipped.first.any { trigger -> trigger(it) } },
            { it -> unzipped.second.any { trigger -> trigger(it) } }
    )
)
}

inline fun <T> List<T>.merge(combiner: (list: List<T>) -> T,
                      mergeStateFn: (state: MergeState, element: T) -> MergeState): List<T> {
    val newlist = mutableListOf<T>()
    val buffer = mutableListOf<T>()

    var mergeState = MergeState.None

    forEach {
        mergeState = mergeStateFn(mergeState, it)
        when (mergeState) {
            MergeState.Open -> {
                mergeState = MergeState.Continue
                buffer.clear()
                buffer.add(it)
            }
            MergeState.Continue -> {
                buffer.add(it)
            }
            MergeState.Close -> {
                mergeState = MergeState.None
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