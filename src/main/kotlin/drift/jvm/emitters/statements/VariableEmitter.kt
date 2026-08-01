/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements

import drift.hir.HIRVariable
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.expressions.ExpressionEmitter
import drift.jvm.emitters.opcodes.StoreOpcode
import drift.jvm.emitters.types.helpers.TypeConverter.getSlotWidth
import language.Namespace


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class VariableEmitter(
    val namespace: Namespace,
    val context: EmitContext) : SinkEmitter<HIRVariable> {

    override fun emit(node: HIRVariable) {
        val slotWidth = getSlotWidth(node.type)
        val slot = context.slotsManager
            .allocateAndLink(node.hirId, slotWidth)

        node.initialValue?.let {
            ExpressionEmitter(namespace, context)
                .emit(it)

            val opcode = StoreOpcode.fromType(node.type)

            context.methodVisitor
                .visitVarInsn(opcode, slot.index)
        }
    }
}