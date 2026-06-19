/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.specials

import drift.hir.HIRVariable
import drift.jvm.emitters.SinkEmitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter.toAsmType
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_STATIC


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class TopLevelVariableEmitter(
    private val classWriter: ClassWriter)
    : SinkEmitter<HIRVariable> {

    override fun emit(node: HIRVariable) {
        val access = TempValues.visibility then ACC_STATIC

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