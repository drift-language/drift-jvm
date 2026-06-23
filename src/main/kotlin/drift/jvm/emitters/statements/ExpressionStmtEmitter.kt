/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements

import drift.hir.HIRExpressionStmt
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.expressions.ExpressionEmitter
import language.Namespace


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class ExpressionStmtEmitter(
    private val namespace: Namespace,
    private val context: EmitContext) : SinkEmitter<HIRExpressionStmt> {

    override fun emit(node: HIRExpressionStmt) {
        ExpressionEmitter(namespace, context)
            .emit(node.expression)
    }
}