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

import drift.hir.HIRFunction
import drift.jvm.emitters.Emitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.opcodes.OpcodesPlus.ACC_NOT_STATIC
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter.formatTypes
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_STATIC


/**
 * This emitter class permits emitting [HIRFunction] by writing it
 * to a [ClassWriter] instance.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class MethodEmitter(
    private val classWriter: ClassWriter)
    : Emitter<HIRFunction, Unit> {

    override fun emit(node: HIRFunction) {
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

        with(methodVisitor) {
            visitCode()

            // TODO: Emit statements

            visitMaxs(0, 0)
            visitEnd()
        }
    }
}