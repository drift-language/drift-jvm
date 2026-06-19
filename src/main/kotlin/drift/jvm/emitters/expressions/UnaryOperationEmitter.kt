/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.expressions

import drift.hir.HIRPrimitiveType
import drift.hir.HIRUnaryOp
import drift.hir.PrimitiveKind
import drift.hir.UnaryOperator.NEGATE
import drift.hir.UnaryOperator.NOT
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.expressions.helpers.OperationHelper.emitOperand
import language.Namespace
import org.objectweb.asm.Opcodes.*


/**
 * This sink emitter handles unary operation
 * expressions.
 *
 * This emitter handles only native unary operations.
 * User-defined ones are treated as method calls.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class UnaryOperationEmitter(
    private val namespace: Namespace,
    private val context: EmitContext) : SinkEmitter<HIRUnaryOp> {

    override fun emit(node: HIRUnaryOp) =
        when (node.operator) {
            NEGATE  -> emitNegate(node)
            NOT     -> emitNot(node)
        }

    /**
     * Emit arithmetic negation operation.
     *
     * ```drift
     * -1
     * ```
     */
    private fun emitNegate(node: HIRUnaryOp) {
        val expressionEmitter = ExpressionEmitter(namespace, context)

        val opcode = when (val type = node.type) {
            is HIRPrimitiveType -> when (type.kind) {
                PrimitiveKind.BOOL,
                PrimitiveKind.INT,
                PrimitiveKind.UINT      -> INEG

                PrimitiveKind.INT64     -> LNEG

                PrimitiveKind.STRING,
                PrimitiveKind.NULL      -> error("Cannot negate a reference type")

                else                    -> error("Unsupported type")
            }

            else -> error("Unsupported type")
        }

        with(context.methodVisitor) {
            emitOperand(
                context.methodVisitor,
                expressionEmitter,
                node.operand,
                node.type)

            visitInsn(opcode)
        }
    }

    /**
     * Emit boolean negation operation.
     *
     * ```drift
     * let state = true
     * !state       // equals false
     * ```
     */
    private fun emitNot(node: HIRUnaryOp) {
        val expressionEmitter = ExpressionEmitter(namespace, context)

        with(context.methodVisitor) {
            emitOperand(
                context.methodVisitor,
                expressionEmitter,
                node.operand,
                node.type)

            visitInsn(ICONST_1)

            visitInsn(IXOR)

            // NOTE: !state <=> state (XOR) 1
            //  | state | k | XOR   |
            //  |-------|---|-------|
            //  | true  | 1 | false |
            //  | false | 1 | true  |
        }
    }
}