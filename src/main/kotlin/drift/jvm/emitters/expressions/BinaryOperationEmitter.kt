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

import drift.hir.BinaryOperator
import drift.hir.HIRBinaryOp
import drift.hir.HIRExpression
import drift.hir.HIRPrimitiveType
import drift.hir.HIRType
import drift.hir.PrimitiveKind
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.InnerEmitter
import drift.jvm.emitters.expressions.helpers.OperationHelper.emitOperand
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*


/**
 * This inner emitter handles binary operation
 * expressions.
 *
 * This emitter handles only native binary operations.
 * User-defined ones are treated as method calls.
 *
 * @author Jonathan (GitHub: belicfr)
 * @see HIRBinaryOp
 */
class BinaryOperationEmitter(
    private val context: EmitContext) : InnerEmitter<HIRBinaryOp> {

    override fun emit(node: HIRBinaryOp) {
        when {
            isArithmeticOperation(node.operator) ->
                ArithmeticOperationEmitter().emit(node)

            isComparisonOperation(node.operator) ->
                ComparisonOperationEmitter().emit(node)

            isLogicalOperation(node.operator) ->
                LogicalOperationEmitter().emit(node)


            else ->
                error("Unexpected operator")
        }
    }

    /**
     * @return If the provided [BinaryOperator] is an arithmetic
     *         operator.
     */
    private fun isArithmeticOperation(operator: BinaryOperator) =
        operator in setOf(
            BinaryOperator.ADD,
            BinaryOperator.SUB,
            BinaryOperator.MUL,
            BinaryOperator.DIV,
            BinaryOperator.MOD)

    /**
     * @return If the provided [BinaryOperator] results in a
     *         boolean value.
     */
    private fun isComparisonOperation(operator: BinaryOperator) =
        operator in setOf(
            BinaryOperator.EQ,
            BinaryOperator.NEQ,
            BinaryOperator.LT,
            BinaryOperator.LTE,
            BinaryOperator.GT,
            BinaryOperator.GTE)

    private fun isLogicalOperation(operator: BinaryOperator) =
        operator in setOf(
            BinaryOperator.AND,
            BinaryOperator.OR)


    /**
     * Inner emitter dedicated to all arithmetic binary operations.
     *
     * @author Jonathan (GitHub: belicfr)
     * @see BinaryOperationEmitter
     */
    private inner class ArithmeticOperationEmitter : InnerEmitter<HIRBinaryOp> {

        override fun emit(node: HIRBinaryOp) {
            val opcode: Int = when (node.operator) {
                BinaryOperator.ADD  -> getAdditionOpcode(node)
                BinaryOperator.SUB  -> getSubstractionOpcode(node)

                BinaryOperator.MUL  -> getMultiplicationOpcode(node)
                BinaryOperator.DIV  -> getDivisionOpcode(node)
                BinaryOperator.MOD  -> getModuloOpcode(node)

                else                -> error("Not an arithmetic operator")
            }

            val expressionEmitter = ExpressionEmitter(context)

            emitOperand(
                context.methodVisitor,
                expressionEmitter,
                node.left,
                node.type)

            emitOperand(
                context.methodVisitor,
                expressionEmitter,
                node.right,
                node.type)

            context
                .methodVisitor
                .visitInsn(opcode)
        }

        private fun getOpcode(type: HIRType, context: ContextOpcodes) : Int =
            when (type) {
                is HIRPrimitiveType -> when (type.kind) {
                    PrimitiveKind.BOOL,
                    PrimitiveKind.INT,
                    PrimitiveKind.UINT  -> context.i32
                    PrimitiveKind.INT64 -> context.l64

                    else                -> error("Unsupported type for native addition")
                }

                else -> error("Unsupported type for native addition")
            }

        private fun getAdditionOpcode(node: HIRBinaryOp) : Int {
            val context = ContextOpcodes(
                i32 = IADD,
                l64 = LADD)

            return getOpcode(node.type, context)
        }

        private fun getSubstractionOpcode(node: HIRBinaryOp) : Int {
            val context = ContextOpcodes(
                i32 = ISUB,
                l64 = LSUB)

            return getOpcode(node.type, context)
        }

        private fun getMultiplicationOpcode(node: HIRBinaryOp) : Int {
            val context = ContextOpcodes(
                i32 = IMUL,
                l64 = LMUL)

            return getOpcode(node.type, context)
        }

        private fun getDivisionOpcode(node: HIRBinaryOp) : Int {
            val context = ContextOpcodes(
                i32 = IDIV,
                l64 = LDIV)

            return getOpcode(node.type, context)
        }

        private fun getModuloOpcode(node: HIRBinaryOp) : Int {
            val context = ContextOpcodes(
                i32 = IREM,
                l64 = LREM)

            return getOpcode(node.type, context)
        }


        private inner class ContextOpcodes(
            val i32: Int,
            val l64: Int)
    }


    /**
     * Inner emitter dedicated to all comparison binary operations.
     *
     * @author Jonathan (GitHub: belicfr)
     * @see BinaryOperationEmitter
     */
    private inner class ComparisonOperationEmitter : InnerEmitter<HIRBinaryOp> {

        override fun emit(node: HIRBinaryOp) {
            val inversedConditionOpcode: Int = when (node.operator) {
                BinaryOperator.EQ   -> IF_ICMPNE        // inverse of '==' is '!='
                BinaryOperator.NEQ  -> IF_ICMPEQ        // inverse of '!=' is '=='

                BinaryOperator.LT   -> IF_ICMPGE        // inverse of '<' is '>='
                BinaryOperator.LTE  -> IF_ICMPGT        // inverse of '<=' is '>'

                BinaryOperator.GT   -> IF_ICMPLE        // inverse of '>' is '<='
                BinaryOperator.GTE  -> IF_ICMPLT        // inverse of '>=' is '<'

                else                -> error("Not a comparison operator")
            }

            val expressionEmitter = ExpressionEmitter(context)

            emitOperand(
                context.methodVisitor,
                expressionEmitter,
                node.left,
                node.type)

            emitOperand(
                context.methodVisitor,
                expressionEmitter,
                node.right,
                node.type)

            with(context.methodVisitor) {
                val falseLabel = Label()
                val endLabel = Label()

                visitJumpInsn(inversedConditionOpcode, falseLabel)
                // NOTE: If the comparison is FALSE,
                //  a jump to the false branch label is done.

                visitInsn(ICONST_1)
                // NOTE: Put '1' (TRUE) in the stack, this statement
                //  is reached if the previous jump statement is
                //  not executed (condition equals TRUE)

                visitJumpInsn(GOTO, endLabel)
                // NOTE: Once '1' is put, the runtime jumps to
                //  the end label.

                visitLabel(falseLabel)
                visitInsn(ICONST_0)
                // NOTE: If the false branch label is reached,
                //  '0' (FALSE) is put in the stack.

                visitLabel(endLabel)
            }
        }
    }


    /**
     * Inner emitter dedicated to all logical binary operations.
     *
     * @author Jonathan (GitHub: belicfr)
     * @see BinaryOperationEmitter
     */
    private inner class LogicalOperationEmitter : InnerEmitter<HIRBinaryOp> {

        override fun emit(node: HIRBinaryOp) =
            when (node.operator) {
                BinaryOperator.AND  -> emitAnd(node)
                BinaryOperator.OR   -> emitOr(node)

                else                -> error("Not a logical operator")
            }

        private fun emitAnd(node: HIRBinaryOp) {
            val expressionEmitter = ExpressionEmitter(context)

            with(context.methodVisitor) {
                val falseLabel = Label()
                val endLabel = Label()

                emitOperand(
                    context.methodVisitor,
                    expressionEmitter,
                    node.left,
                    node.type)

                visitJumpInsn(IFEQ, falseLabel)
                // NOTE: If the left operand is FALSE (== 0),
                //  jump to the false branch label.

                emitOperand(
                    context.methodVisitor,
                    expressionEmitter,
                    node.right,
                    node.type)

                visitJumpInsn(IFEQ, falseLabel)
                // NOTE: If the right operand is FALSE (== 0),
                //  jump to the false branch label.

                visitInsn(ICONST_1)
                // NOTE: If no one jump is done previously,
                //  put '1' (TRUE) into the stack.

                visitJumpInsn(GOTO, endLabel)

                visitLabel(falseLabel)
                visitInsn(ICONST_0)
                // NOTE: If the false branch label is reached,
                //  put '0' (FALSE) into the stack.

                visitLabel(endLabel)
            }
        }

        private fun emitOr(node: HIRBinaryOp) {
            val expressionEmitter = ExpressionEmitter(context)

            with(context.methodVisitor) {
                val trueLabel = Label()
                val endLabel = Label()

                emitOperand(
                    context.methodVisitor,
                    expressionEmitter,
                    node.left,
                    node.type)

                visitJumpInsn(IFNE, trueLabel)
                // NOTE: If the left operand is TRUE (!= 0),
                //  jump to the true branch label.

                emitOperand(
                    context.methodVisitor,
                    expressionEmitter,
                    node.right,
                    node.type)

                visitJumpInsn(IFNE, trueLabel)
                // NOTE: If the right operand is TRUE (!= 0),
                //  jump to the true branch label.

                visitInsn(ICONST_0)
                // NOTE: If no one jump is done previously,
                //  put '0' (FALSE) into the stack.

                visitJumpInsn(GOTO, endLabel)

                visitLabel(trueLabel)
                visitInsn(ICONST_1)
                // NOTE: If the true branch label is reached,
                //  put '1' (TRUE) into the stack.

                visitLabel(endLabel)
            }
        }
    }
}