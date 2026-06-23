/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.expressions

import drift.hir.AssignTarget
import drift.hir.FieldTarget
import drift.hir.HIRAssign
import drift.hir.VariableTarget
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.types.helpers.TypeConverter.toAsmType
import language.Namespace
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Opcodes.PUTSTATIC


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class AssignEmitter(
    private val namespace: Namespace,
    private val context: EmitContext)
    : SinkEmitter<HIRAssign> {

    override fun emit(node: HIRAssign) {
        when (val target = node.target) {
            is FieldTarget      -> emitFieldAssign(node, target)
            is VariableTarget   -> emitVariableAssign(node, target)
        }
    }

    private fun emitFieldAssign(node: HIRAssign, target: FieldTarget) {
        ExpressionEmitter(namespace, context)
            .emit(node.value)

        emitFieldAssignInstruction(
            node,
            target,
            isStatic = target.fieldOffset == -1)
    }

    private fun emitFieldAssignInstruction(
        node: HIRAssign,
        target: FieldTarget,
        isStatic: Boolean) {

        val opcode =
            if (isStatic) PUTSTATIC
            else PUTFIELD

        val owner = namespace.getQualifiedName()
        val descriptor = toAsmType(node.type)

        context.methodVisitor.visitFieldInsn(
            opcode,
            owner,
            target.fieldName,
            descriptor)
    }

    private fun emitVariableAssign(node: HIRAssign, target: VariableTarget) {
//        val isTopLevel = context
//            .nodesManager
//            .topLevelVariablesDefinitions[]
    }

    private fun emitTopLevelVariable(node: HIRAssign) {
        TODO()
    }
}