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

import drift.hir.HIRFunction
import drift.hir.HIRMethod
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.opcodes.OpcodesPlus.ACC_NOT_STATIC
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter
import drift.jvm.emitters.types.helpers.TypeConverter.formatTypes
import language.Namespace
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_STATIC


/**
 * This emitter class permits emitting [HIRFunction].
 *
 * @author Jonathan (GitHub: belicfr)
 */
class FunctionEmitter(
    private val namespace: Namespace,
    private val classWriter: ClassWriter,
    private val nodesManager: NodesManager)
    : SinkEmitter<HIRFunction> {

    override fun emit(node: HIRFunction) {
        val access = TempValues.visibility then ACC_STATIC
        // NOTE: functions are

        val inputTypes = node
            .parameters
            .map { it.type }

        val outputType = node.returnType

        val methodVisitor = classWriter.visitMethod(
            access,
            node.name,
            formatTypes(inputTypes, outputType),
            TempValues.signature,
            TempValues.exceptions)

        val slotsManager = SlotsManager()

        if (!node.isStatic)
            slotsManager.allocate(SlotsManager.SIMPLE_WIDTH)
            // NOTE: in an instance context, '$this' needs to be allocated as
            //  the 0-indexed slot.

        for (param in node.parameters) {
            val slotWidth = TypeConverter.getSlotWidth(param.type)
            slotsManager.allocateAndLink(param.hirId, slotWidth)
        }

        val context = EmitContext(
            methodVisitor = methodVisitor,
            slotsManager = slotsManager,
            nodesManager = nodesManager)

        with(methodVisitor) {
            visitCode()

            node.body
                .forEach(StatementEmitter(namespace, context)::emit)

            visitMaxs(0, 0)
            visitEnd()
        }
    }
}