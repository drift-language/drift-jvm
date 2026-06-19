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

import drift.hir.HIRNode
import org.objectweb.asm.Opcodes


/**
 * Base interface for all the emitting process of a HIR node
 * to another form (like bytecode or assembly).
 *
 * Some emitters will return [ByteArray] to make `.class` files.
 * Others may change states without returning anything.
 *
 * @param NODE Type of HIRNode to emit.
 * @param R Return type of the emission process.
 *         It depends on how each emitting logic is implemented,
 *         which could be a [ByteArray], a string, etc.
 *
 * @author Jonathan (GitHub: belicfr)
 */
interface Emitter<NODE: HIRNode, R> {

    companion object {

        /** Regarding `jvmToolchain` version, [Opcodes.V21] is used. */
        const val OPCODE_VERSION = Opcodes.V21
    }


    /**
     * Emit the given [HIRNode] to its final form.
     *
     * @param node The node to emit.
     * @return The emitted result, which can be different depending on the emitter.
     *         The emitter can return [Unit] — nothing.
     */
    fun emit(node: NODE) : R
}