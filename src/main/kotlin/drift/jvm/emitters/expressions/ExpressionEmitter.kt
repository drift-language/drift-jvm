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

import drift.hir.HIRBinaryOp
import drift.hir.HIRCall
import drift.hir.HIRExpression
import drift.hir.HIRLiteral
import drift.hir.HIRUnaryOp
import drift.hir.HIRVariableRef
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter


/**
 * This sink emitter handles expressions.
 * Regarding of the expression's kind, this emitter
 * dispatches to a dedicated sink emitter.
 *
 * @author Jonathan (GitHub: belicfr)
 * @see HIRExpression
 */
class ExpressionEmitter(
    private val namespace: String,
    private val context: EmitContext)
    : SinkEmitter<HIRExpression> {

    override fun emit(node: HIRExpression) {
        when (node) {
            is HIRLiteral -> {
                LiteralEmitter(context)
                    .emit(node)
            }

            is HIRVariableRef -> {
                VariableReferenceEmitter(context)
                    .emit(node)
            }

            is HIRBinaryOp -> {
                BinaryOperationEmitter(namespace, context)
                    .emit(node)
            }

            is HIRUnaryOp -> {
                UnaryOperationEmitter(namespace, context)
                    .emit(node)
            }

            is HIRCall -> {
                CallEmitter(namespace, context)
                    .emit(node)
            }


            else -> error("Unexpected expression kind")
        }
    }
}