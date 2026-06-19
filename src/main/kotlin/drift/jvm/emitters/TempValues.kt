/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters

import org.objectweb.asm.Opcodes


/**
 * A collection of temporary values used during emitting phases;
 * in waiting for their respective final implementations.
 *
 * @author Jonathan (GitHub: belicfr)
 */
object TempValues {

    /**
     * At this time, Drift does not handle explicit visibilities.
     * Everything is PUBLIC.
     *
     * Once supported, the visibility must be handled manually,
     * regarding the context.
     */
    const val visibility = Opcodes.ACC_PUBLIC

    /**
     * Signature contains information about generic types if any.
     * For now, it is not supported, so the variable is set to `null`.
     */
    val signature = null

    /**
     * Exception throwing is not supported yet.
     * So an empty array is returned for now.
     */
    val exceptions = emptyArray<String>()

    /**
     * For now, Drift does not support compile-time
     * fields. For this reason, `null` is used.
     */
    val fieldCompileTimeValue = null
}