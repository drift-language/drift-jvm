/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters.expressions

import drift.hir.HIRClassType
import drift.hir.HIROptionalType
import drift.hir.HIRPrimitiveType
import drift.hir.HIRUnionType
import drift.hir.HIRVariableRef
import drift.hir.PrimitiveKind
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.InnerEmitter
import org.objectweb.asm.Opcodes.*


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class VariableReferenceEmitter(
    private val context: EmitContext) : InnerEmitter<HIRVariableRef> {

    override fun emit(node: HIRVariableRef) {
        val loadOpcode = when (val type = node.type) {
            is HIRPrimitiveType -> getLoadOpcodeFromPrimitive(type.kind)
            is HIRClassType,
            is HIROptionalType,
            is HIRUnionType     -> ALOAD

            else                -> error("Unexpected type")
        }
        val slotIndex = context
            .slotsManager
            .get(node.definitionHirId)

        if (slotIndex == null)
            error("Unknown variable [DEF_HIR_ID=${node.definitionHirId}]")

        with(context.methodVisitor) {
            visitVarInsn(
                loadOpcode,
                slotIndex)
        }
    }

    private fun getLoadOpcodeFromPrimitive(primitiveKind: PrimitiveKind) =
        when (primitiveKind) {
            PrimitiveKind.BOOL,
            PrimitiveKind.INT,
            PrimitiveKind.UINT      -> ILOAD
            PrimitiveKind.INT64     -> LLOAD
            PrimitiveKind.STRING,
            PrimitiveKind.NULL      -> ALOAD

            else                    -> error("Unexpected primitive")
        }
}