/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.types.helpers

import drift.hir.HIRPrimitiveType
import drift.hir.HIRType
import drift.jvm.emitters.managers.SlotsManager


/**
 *
 *
 * @author Jonathan (GitHub: belicfr)
 */
object TypeConverter {

    /**
     * Get the (JVM) ASM type from a given HIR type.
     *
     * @param hirType HIR type to convert.
     *
     * @return the converted type.
     */
    fun toAsmType(hirType: HIRType) : String {
        return if (hirType is HIRPrimitiveType) {
            PrimitiveType
                .fromHirType(hirType.kind)
                .asmType
        } else {
            PrimitiveType
                .ANY
                .asmType
        }
    }

    /**
     * Get a formatted descriptor string from input types and an output type.
     *
     * The formatted string respects the JVM descriptor syntax: ``(...)...``.
     *
     * @param input input types.
     * @param output output type.
     *
     * @return formatted type descriptor.
     */
    fun formatTypes(input: Collection<HIRType>, output: HIRType) : String {
        val formattedInputTypes = input
            .joinToString(transform = this::toAsmType)
        val formattedOutputType = toAsmType(output)

        return "($formattedInputTypes)$formattedOutputType"
    }


    /**
     * Return the necessary slot width for the given type.
     * - A primitive type needs [SlotsManager.SIMPLE_WIDTH] if non-double, else
     * [SlotsManager.DOUBLE_WIDTH].
     * - A non-primitive one needs [SlotsManager.SIMPLE_WIDTH].
     *
     * @return The computed slot width.
     */
    fun getSlotWidth(hirType: HIRType) : Int =
        if (hirType is HIRPrimitiveType) PrimitiveType.fromHirType(hirType.kind).getSlotWidth()
        else SlotsManager.SIMPLE_WIDTH
}