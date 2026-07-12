/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.opcodes

import drift.hir.*
import org.objectweb.asm.Opcodes.*


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
object StoreOpcode {

    fun fromType(type: HIRType) : Int =
        when (type) {
            is HIRPrimitiveType -> getStoreOpcodeFromPrimitive(type.kind)
            is HIRClassType,
            is HIROptionalType,
            is HIRUnionType     -> ASTORE

            else                -> error("Unexpected type")
        }


    /**
     * Attempt retrieving a `*STORE` [org.objectweb.asm.Opcodes] from a
     * [PrimitiveKind] case.
     *
     * @param primitiveKind Primitive type used to retrieve the
     *                      [org.objectweb.asm.Opcodes].
     * @return [org.objectweb.asm.Opcodes] value if found.
     *         If no one satisfies the provided [PrimitiveKind],
     *         an exception is thrown.
     * @see PrimitiveKind
     * @see org.objectweb.asm.Opcodes
     */
    private fun getStoreOpcodeFromPrimitive(primitiveKind: PrimitiveKind) =
        when (primitiveKind) {
            PrimitiveKind.BOOL,
            PrimitiveKind.INT,
            PrimitiveKind.UINT      -> ISTORE
            PrimitiveKind.INT64     -> LSTORE
            PrimitiveKind.STRING,
            PrimitiveKind.NULL      -> ASTORE

            else                    -> error("Unexpected primitive")
        }
}