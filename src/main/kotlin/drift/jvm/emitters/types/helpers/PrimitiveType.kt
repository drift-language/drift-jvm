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

import drift.hir.PrimitiveKind
import drift.jvm.emitters.managers.SlotsManager


const val stringAsmType = "Ljava/lang/String;"
const val objectAsmType = "Ljava/lang/Object;"


/**
 * Primitive types conversion between HIR and JVM.
 *
 * @author Jonathan (GitHub: belicfr)
 */
enum class PrimitiveType(
    val hirType: PrimitiveKind?,
    val asmType: String) {

    VOID(PrimitiveKind.VOID, "V"),

    INT(PrimitiveKind.INT, "I"),
    UINT(PrimitiveKind.UINT, "I"),

    LONG(PrimitiveKind.INT64, "L"),

    BOOLEAN(PrimitiveKind.BOOL, "Z"),

    STRING(PrimitiveKind.STRING, stringAsmType),

    ANY(null, objectAsmType),

    NULL(PrimitiveKind.NULL, objectAsmType)
    ;


    /**
     * Needed width to allocate in [SlotsManager] for the current type.
     *
     * @return Width to allocate.
     */
    fun getSlotWidth() =
        if (this == LONG)   SlotsManager.DOUBLE_WIDTH
        else                SlotsManager.SIMPLE_WIDTH


    companion object {

        fun fromHirType(hirType: PrimitiveKind) : PrimitiveType =
            when (hirType) {
                PrimitiveKind.VOID      -> VOID
                PrimitiveKind.INT       -> INT
                PrimitiveKind.UINT      -> UINT
                PrimitiveKind.INT64     -> LONG
                PrimitiveKind.BOOL      -> BOOLEAN
                PrimitiveKind.STRING    -> STRING
                PrimitiveKind.NULL      -> NULL
            }

        fun fromAsmType(asmType: String) : PrimitiveType =
            when (asmType) {
                "V"             -> VOID
                "I"             -> INT
                "Z"             -> BOOLEAN
                stringAsmType   -> STRING
                objectAsmType   -> error("'$objectAsmType' is ambiguous to be translated to a HIR primitive type.")

                else -> error("Unknown asm type: $asmType")
            }
    }
}