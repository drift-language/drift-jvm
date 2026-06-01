/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters.sugar


/**
 * Syntaxic sugar to make more readable
 * [org.objectweb.asm.Opcodes] unions.
 *
 * Before:
 * ```kotlin
 * val access = ACC_PUBLIC or ACC_STATIC
 * // NOTE: 'or' is BITWISE-OR operator.
 * ```
 *
 * Now:
 * ```kotlin
 * val access = ACC_PUBLIC then ACC_STATIC
 * ```
 *
 * **Important!**
 * This infix should not be used in another case
 * than [org.objectweb.asm.Opcodes] union.
 * Its behavior could be changed at anytime.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
infix fun Int.then(flag: Int) =
    this or flag