/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements

import drift.hir.HIRClass
import drift.hir.HIRReturn
import drift.hir.HIRStatement
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class StatementEmitter(
    private val namespace: String,
    private val context: EmitContext)
    : SinkEmitter<HIRStatement> {

    override fun emit(node: HIRStatement) {
        when (node) {
            is HIRClass -> ClassEmitter(namespace).emit(node)
            is HIRReturn -> ReturnEmitter(namespace, context).emit(node)

            else        -> error("Unexpected statement")
        }
    }
}