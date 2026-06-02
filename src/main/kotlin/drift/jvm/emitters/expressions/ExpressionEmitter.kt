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

import drift.hir.HIRExpression
import drift.hir.HIRLiteral
import drift.hir.HIRVariableRef
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.InnerEmitter
import drift.jvm.emitters.SlotsManager
import org.objectweb.asm.MethodVisitor


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
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


            // TODO: other expression kinds

            else -> error("Unexpected expression kind")
        }
    }
}