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

import drift.hir.HIRClass
import drift.jvm.emitters.Emitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.common.ClassGenerator
import drift.jvm.emitters.common.ClassGenerator.ClassMembers
import drift.jvm.emitters.types.helpers.ClassHelper
import drift.jvm.emitters.types.helpers.ClassHelper.getInternalClassName
import language.Namespace
import org.objectweb.asm.ClassWriter


/**
 * This emitter class permits emitting [HIRClass] nodes to [ByteArray].
 *
 * All Drift classes are emitted to bytecode which can then be written
 * into a `.class` file and run on JVM.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class ClassEmitter(
    private val namespace: Namespace)
    : Emitter<HIRClass, ByteArray> {

    override fun emit(node: HIRClass) : ByteArray {
        val members = ClassMembers(
            fields = node.fields,
            staticFields = node.staticFields,
            methods = node.methods,
            staticMethods = node.staticMethods)

        val classWriter = ClassGenerator(namespace)
            .generate(node.name, members)

        classWriter.visitEnd()

        return classWriter.toByteArray()
    }
}