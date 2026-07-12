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

import drift.hir.HIRClass
import drift.hir.HIRExpressionStmt
import drift.hir.HIRReturn
import drift.hir.HIRStatement
import drift.hir.HIRVariable
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import language.Namespace


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class StatementEmitter(
    private val namespace: Namespace,
    private val context: EmitContext)
    : SinkEmitter<HIRStatement> {

    override fun emit(node: HIRStatement) {
        when (node) {
            is HIRClass -> error("Class definitions should be handled before.")

            is HIRReturn ->
                ReturnEmitter(namespace, context).emit(node)

            is HIRExpressionStmt ->
                ExpressionStmtEmitter(namespace, context).emit(node)

            is HIRVariable ->
                VariableEmitter(namespace, context).emit(node)


            else -> error("Unexpected statement")
        }
    }
}