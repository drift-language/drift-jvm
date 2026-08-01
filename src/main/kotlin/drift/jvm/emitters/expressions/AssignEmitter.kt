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

import drift.hir.FieldTarget
import drift.hir.HIRAssign
import drift.hir.LocalVariableTarget
import drift.hir.TopLevelVariableTarget
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.opcodes.StoreOpcode
import drift.jvm.emitters.types.helpers.ClassHelper
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
        ExpressionEmitter(namespace, context)
            .emit(node.value)

        when (val target = node.target) {
            is FieldTarget              -> emitFieldAssign(node, target)
            is TopLevelVariableTarget   -> emitTopLevelVariable(node, target)
            is LocalVariableTarget      -> emitLocalVariable(node, target)
        }
    }

    private fun emitFieldAssign(node: HIRAssign, target: FieldTarget) {
        val opcode =
            if (target.fieldOffset == -1) PUTSTATIC
            else PUTFIELD

        val owner = target.ownerNamespace.getNamespace()
        val descriptor = toAsmType(node.type)

        context.methodVisitor.visitFieldInsn(
            opcode,
            owner,
            target.fieldName,
            descriptor)
    }

    private fun emitTopLevelVariable(node: HIRAssign, target: TopLevelVariableTarget) {
        val opcode = PUTSTATIC
        val owner = ClassHelper.getSyntheticClassName(target.ownerNamespace)
        val descriptor = toAsmType(node.type)

        context.methodVisitor.visitFieldInsn(
            opcode,
            owner,
            target.name,
            descriptor)
    }

    private fun emitLocalVariable(node: HIRAssign, target: LocalVariableTarget) {
        val opcode = StoreOpcode.fromType(node.type)
        val slot = context
            .slotsManager
            .getSlotAllocation(target.definitionHirId)
            ?: error("Unknown variable: '${target.name}'")

        context.methodVisitor.visitVarInsn(
            opcode,
            slot.index)
    }
}