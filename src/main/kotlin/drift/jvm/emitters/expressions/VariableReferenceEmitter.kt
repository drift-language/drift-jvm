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

import drift.hir.*
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import org.objectweb.asm.Opcodes.*


/**
 * This sink emitter handles variable reference expressions.
 *
 * @author Jonathan (GitHub: belicfr)
 * @see HIRVariableRef
 */
class VariableReferenceEmitter(
    private val context: EmitContext) : SinkEmitter<HIRVariableRef> {

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

    /**
     * Attempt retrieving a `*LOAD` [org.objectweb.asm.Opcodes]
     * from a [PrimitiveKind] case.
     *
     * @param primitiveKind Primitive type used to retrieve
     *                      the [org.objectweb.asm.Opcodes].
     * @return [org.objectweb.asm.Opcodes] value if found.
     *         If no one satisfies the provided [PrimitiveKind],
     *         an exception is thrown.
     * @see PrimitiveKind
     * @see org.objectweb.asm.Opcodes
     */
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