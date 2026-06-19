/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.expressions.helpers

import drift.hir.HIRExpression
import drift.hir.HIRPrimitiveType
import drift.hir.HIRType
import drift.hir.PrimitiveKind
import drift.jvm.emitters.expressions.ExpressionEmitter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.I2L


/**
 * Utility class dedicated to operations handling.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
object OperationHelper {

    /**
     * Emit the provided operand.
     * If the node's type is a 32-bits integer (signed or not),
     * and the operand's one a 64-bits integer, the operand stack
     * value is widened from integer to long using [I2L] opcode.
     *
     * @param expressionEmitter Expression emitter instance used
     *                          to emit the operand.
     * @param operand The operand to emit.
     * @param nodeType The type expected for the operation.
     */
    fun emitOperand(
        methodVisitor: MethodVisitor,
        expressionEmitter: ExpressionEmitter,
        operand: HIRExpression,
        nodeType: HIRType) {

        expressionEmitter.emit(operand)

        val isOperand32BitsInteger =
            operand.type == HIRPrimitiveType(PrimitiveKind.INT) ||
                    operand.type == HIRPrimitiveType(PrimitiveKind.UINT)

        val isOperationExpectedTypeLong =
            nodeType == HIRPrimitiveType(PrimitiveKind.INT64)

        if (isOperand32BitsInteger && isOperationExpectedTypeLong) {
            methodVisitor.visitInsn(I2L)
        } else if (operand.type != nodeType) {
            error("Unsupported operation, incompatible types")
        }
    }
}