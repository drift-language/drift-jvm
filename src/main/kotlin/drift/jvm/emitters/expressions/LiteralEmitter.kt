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

import drift.hir.HIRLiteral
import drift.hir.HIRPrimitiveType
import drift.hir.PrimitiveKind
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.opcodes.OpcodesPlus.BIPUSH_MAX
import drift.jvm.emitters.opcodes.OpcodesPlus.BIPUSH_MIN
import drift.jvm.emitters.opcodes.OpcodesPlus.SIPUSH_MAX
import drift.jvm.emitters.opcodes.OpcodesPlus.SIPUSH_MIN
import org.objectweb.asm.Opcodes.*


/**
 * This sink emitter handles literal expressions.
 * 
 * @author Jonathan (GitHub: belicfr)
 * @see HIRLiteral
 */
class LiteralEmitter(
    private val context: EmitContext)
    : SinkEmitter<HIRLiteral> {

    override fun emit(node: HIRLiteral) {
        when (node.type) {
            is HIRPrimitiveType -> emitPrimitive(node)

            else -> error("Unexpected literal type")
        }
    }

    /**
     * Handle a primitive value from a literal expression
     * using native JVM primitive methods and opcodes.
     */
    private fun emitPrimitive(node: HIRLiteral) {
        if (node.value == null) {
            context
                .methodVisitor
                .visitInsn(ACONST_NULL)

            return
        }

        if (node.type !is HIRPrimitiveType)
            error("Unexpected primitive type")

        when ((node.type as HIRPrimitiveType).kind) {
            PrimitiveKind.BOOL -> {
                val opcode =
                    if (node.value as Boolean) ICONST_1
                    else ICONST_0

                context
                    .methodVisitor
                    .visitInsn(opcode)
            }
            PrimitiveKind.INT64 -> {
                context
                    .methodVisitor
                    .visitLdcInsn(node.value as Long)
            }
            PrimitiveKind.INT, PrimitiveKind.UINT ->
                when (val value: Int = node.value as Int) {
                    in ICONST_M1..ICONST_5 -> {
                        context
                            .methodVisitor
                            .visitInsn(ICONST_0 + value)
                    }
                    in BIPUSH_MIN..BIPUSH_MAX -> {
                        context
                            .methodVisitor
                            .visitIntInsn(BIPUSH, value)
                    }
                    in SIPUSH_MIN..SIPUSH_MAX -> {
                        context
                            .methodVisitor
                            .visitIntInsn(SIPUSH, value)
                    }

                    else ->
                        context.methodVisitor.visitLdcInsn(value)
                }
            PrimitiveKind.STRING -> {
                context
                    .methodVisitor
                    .visitLdcInsn(node.value as String)
            }
            PrimitiveKind.NULL -> {
                context
                    .methodVisitor
                    .visitInsn(ACONST_NULL)
            }
            PrimitiveKind.VOID ->
                error("VOID cannot be a literal")
        }
    }
}