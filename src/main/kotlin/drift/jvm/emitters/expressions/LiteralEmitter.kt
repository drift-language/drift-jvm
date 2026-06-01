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
import drift.jvm.emitters.Emitter
import drift.jvm.emitters.opcodes.OpcodesPlus.BIPUSH_MAX
import drift.jvm.emitters.opcodes.OpcodesPlus.BIPUSH_MIN
import drift.jvm.emitters.opcodes.OpcodesPlus.SIPUSH_MAX
import drift.jvm.emitters.opcodes.OpcodesPlus.SIPUSH_MIN
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class LiteralEmitter(
    private val methodVisitor: MethodVisitor) : Emitter<HIRLiteral, Unit> {

    override fun emit(node: HIRLiteral) {
        when (node.type) {
            is HIRPrimitiveType -> emitPrimitive(node)

            else -> error("Unexpected literal type")
        }
    }

    private fun emitPrimitive(node: HIRLiteral) {
        when ((node.type as HIRPrimitiveType).kind) {
            PrimitiveKind.BOOL -> {
                if (node.value == null)
                    TODO("NULL behavior on BOOL literal emitting")

                val value: Boolean = node.value as Boolean
                val opcode =
                    if (value) ICONST_1
                    else ICONST_0

                methodVisitor.visitInsn(opcode)
            }
            PrimitiveKind.INT64 -> {
                if (node.value == null)
                    TODO("NULL behavior on INT64 literal emitting")

                val value: Long = node.value as Long

                methodVisitor.visitLdcInsn(value)
            }
            PrimitiveKind.INT, PrimitiveKind.UINT -> {
                if (node.value == null)
                    TODO("NULL behavior on INT literal emitting")

                when (val value: Int = node.value as Int) {
                    in ICONST_M1..ICONST_5 ->
                        methodVisitor.visitInsn(ICONST_0 + value)
                    in BIPUSH_MIN..BIPUSH_MAX ->
                        methodVisitor.visitIntInsn(BIPUSH, value)
                    in SIPUSH_MIN..SIPUSH_MAX ->
                        methodVisitor.visitIntInsn(SIPUSH, value)

                    else -> methodVisitor.visitLdcInsn(value)
                }
            }
            PrimitiveKind.STRING -> {
                if (node.value == null)
                    TODO("NULL behavior on STRING literal emitting")

                val value: String = node.value as String

                methodVisitor.visitLdcInsn(value)
            }
            PrimitiveKind.NULL ->
                methodVisitor.visitInsn(ACONST_NULL)
            PrimitiveKind.VOID ->
                error("VOID cannot be a literal")
        }
    }
}