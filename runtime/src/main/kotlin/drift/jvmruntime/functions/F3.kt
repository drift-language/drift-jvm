/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvmruntime.functions


interface F3<P1, P2, P3, R> {

    fun apply(p1: P1, p2: P2, p3: P3) : R
}
