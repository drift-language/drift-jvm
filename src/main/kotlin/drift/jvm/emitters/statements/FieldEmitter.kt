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

import drift.hir.HIRField
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.opcodes.OpcodesPlus.ACC_NOT_STATIC
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter.toAsmType
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_STATIC


/**
 * This emitter class permits emitting, in a class definition context,
 * [HIRField] by writing it into a [ClassWriter] instance.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class FieldEmitter(
    private val classWriter: ClassWriter)
    : SinkEmitter<HIRField> {

    override fun emit(node: HIRField) {
        val access = TempValues.visibility then
                if (node.isStatic) ACC_STATIC
                else ACC_NOT_STATIC

        val descriptor = toAsmType(node.type)

        val fieldVisitor = classWriter.visitField(
            access,
            node.name,
            descriptor,
            TempValues.signature,
            TempValues.fieldCompileTimeValue)

        fieldVisitor.visitEnd()
    }
}