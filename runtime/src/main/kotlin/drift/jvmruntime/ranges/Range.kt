/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvmruntime.ranges


/**
 * Represents a range of values from start to end.
 */
data class Range(
    val start: Int,
    val end: Int) : Iterable<Int> {

    override fun iterator(): Iterator<Int> =
        IterationManager()


    /**
     * Manage the iterator of the range.
     */
    inner class IterationManager : Iterator<Int> {

        private var currentValue: Int = start


        override fun next(): Int {
            if (!hasNext())
                throw NoSuchElementException()

            return currentValue++
        }

        override fun hasNext(): Boolean =
            currentValue <= end
    }
}