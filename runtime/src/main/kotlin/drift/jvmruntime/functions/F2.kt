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


interface F2<P1, P2, R> {

    operator fun invoke(p1: P1, p2: P2) : R
}
