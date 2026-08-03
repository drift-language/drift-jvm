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
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.statements.common.emitCallableBody
import drift.jvm.emitters.statements.common.allocateCallableParameterSlots
import drift.jvm.emitters.sugar.then
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
        // NOTE: functions are always declared as static members of the current
        //  [synthetic] class. Nested ones names are prefixed by the parents
        //  names.
        // NOTE: at this moment, Drift does not support visibilities. All
        //  structures are implicitly PUBLIC, including functions. Nested ones
        //  must still public, as their accessibility is defined by the scope
        //  hierarchy. Top-level ones should, later, implement visibilities as
        //  they are directly accessible outside the file.

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

        allocateCallableParameterSlots(slotsManager, node.parameters)

        val emitContext = EmitContext(
            methodVisitor = methodVisitor,
            slotsManager = slotsManager,
            nodesManager = nodesManager)
        emitCallableBody(namespace, emitContext, node.body)
    }
}