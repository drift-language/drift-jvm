/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements.common

import drift.hir.HIRParameter
import drift.hir.HIRStatement
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.statements.StatementEmitter
import drift.jvm.emitters.types.helpers.TypeConverter
import language.Namespace


/**
 * Common logic code from all callable emitters. It allocates the parameter's
 * slot after computing its width.
 */
fun allocateCallableParameterSlot(slotsManager: SlotsManager, parameter : HIRParameter) {
    val slotWidth = TypeConverter.getSlotWidth(parameter.type)
    slotsManager.allocateAndLink(parameter.hirId, slotWidth)
}

/**
 * Allocates a slot for each provided callable parameter.
 */
fun allocateCallableParameterSlots(slotsManager: SlotsManager, parameters: List<HIRParameter>) =
    parameters.forEach { allocateCallableParameterSlot(slotsManager, it) }


/**
 * Emits the provided callable body.
 *
 * @param namespace The namespace provided to the [StatementEmitter] for each
 *                  body statements.
 * @param body AST composed by the callable body's statements.
 */
fun emitCallableBody(namespace: Namespace, emitContext: EmitContext, body: List<HIRStatement>) {
    with(emitContext.methodVisitor) {
        visitCode()

        body.forEach(StatementEmitter(namespace, emitContext)::emit)

        visitMaxs(0, 0)
        visitEnd()
    }
}