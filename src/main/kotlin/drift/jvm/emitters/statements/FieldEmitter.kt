/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters.statements

import drift.hir.HIRField
import drift.jvm.emitters.Emitter
import drift.jvm.emitters.InnerEmitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.opcodes.OpcodesPlus.ACC_NOT_STATIC
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter.toAsmType
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_STATIC


/**
 * This emitter class permits emitting [HIRField] by writing it
 * to a [ClassWriter] instance.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class FieldEmitter(
    private val classWriter: ClassWriter)
    : InnerEmitter<HIRField> {

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