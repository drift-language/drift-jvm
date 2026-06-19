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

object TypeConverter {

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

    fun formatTypes(input: List<HIRType>, output: HIRType) : String {
        val formattedInputTypes = input
            .joinToString(transform = this::toAsmType)
        val formattedOutputType = toAsmType(output)

        return "($formattedInputTypes)$formattedOutputType"
    }
}