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
import drift.hir.HIRExpression
import drift.hir.HIRLiteral
import drift.hir.HIRVariableRef
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.InnerEmitter
import drift.jvm.emitters.SlotsManager
import org.objectweb.asm.MethodVisitor


/**
 * This inner emitter handles expressions.
 * Regarding of the expression's kind, this emitter
 * dispatches to a dedicated inner emitter.
 *
 * @author Jonathan (GitHub: belicfr)
 * @see HIRExpression
 */
class ExpressionEmitter(
    private val context: EmitContext)
    : InnerEmitter<HIRExpression> {

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
                BinaryOperationEmitter(context)
                    .emit(node)
            }


            else -> error("Unexpected expression kind")
        }
    }
}