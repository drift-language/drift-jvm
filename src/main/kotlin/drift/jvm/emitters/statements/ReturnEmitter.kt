/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters.statements

import drift.hir.*
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.expressions.ExpressionEmitter
import org.objectweb.asm.Opcodes.*


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class ReturnEmitter(
    private val namespace: String,
    private val context: EmitContext)
    : SinkEmitter<HIRReturn> {

    override fun emit(node: HIRReturn) {
        if (node.value == null) {
            context.methodVisitor.visitInsn(RETURN)
            // NOTE: In that case, we are handling a VOID
            //  special value (expr = null).
            //  We deduct that the value is VOID by logic,
            //  we finally visit instruction with RETURN (void)
            //  opcode.

            return
        }

        val value = node.value!!

        ExpressionEmitter(namespace, context)
            .emit(value)

        val returnOpcode = when (val type = value.type) {
            is HIRPrimitiveType -> getReturnOpcodeFromPrimitive(type.kind)
            is HIRClassType,
            is HIROptionalType,
            is HIRUnionType     -> ARETURN

            else                -> error("Unexpected type")
        }

        context.methodVisitor.visitInsn(returnOpcode)
    }

    private fun getReturnOpcodeFromPrimitive(primitiveKind: PrimitiveKind) =
        when (primitiveKind) {
            PrimitiveKind.BOOL,
            PrimitiveKind.INT,
            PrimitiveKind.UINT      -> IRETURN
            PrimitiveKind.INT64     -> LRETURN
            PrimitiveKind.STRING,
            PrimitiveKind.NULL      -> ARETURN

            else                    -> error("Unexpected primitive")
        }
}