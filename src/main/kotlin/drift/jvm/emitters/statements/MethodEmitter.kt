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

import drift.hir.HIRMethod
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.opcodes.OpcodesPlus.ACC_NOT_STATIC
import drift.jvm.emitters.statements.common.emitCallableBody
import drift.jvm.emitters.statements.common.allocateCallableParameterSlots
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter.formatTypes
import language.Namespace
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_STATIC


/**
 * This emitter class permits emitting [HIRMethod] in a class
 * context by writing it into a [ClassWriter] instance.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class MethodEmitter(
    private val namespace: Namespace,
    private val classWriter: ClassWriter,
    private val nodesManager: NodesManager)
    : SinkEmitter<HIRMethod> {

    override fun emit(node: HIRMethod) {
        val access = TempValues.visibility then
                if (node.isStatic) ACC_STATIC
                else ACC_NOT_STATIC

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
        
        allocateCallableParameterSlots(slotsManager, node.parameters)

        val emitContext = EmitContext(
            methodVisitor = methodVisitor,
            slotsManager = slotsManager,
            nodesManager = nodesManager)
        emitCallableBody(namespace, emitContext, node.body)
    }
}