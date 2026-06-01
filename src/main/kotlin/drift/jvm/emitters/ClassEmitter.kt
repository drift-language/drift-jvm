/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters

import drift.hir.HIRClass
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes


/**
 * This emitter class permits emitting [HIRClass] nodes to [ByteArray].
 *
 * All Drift classes are emitted to bytecode which can then be written
 * into a `.class` file and run on JVM.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class ClassEmitter(
    private val namespace: String)
    : Emitter<HIRClass, ByteArray> {

    override fun emit(node: HIRClass) : ByteArray {
        val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        // NOTE: [ClassWriter.COMPUTE_FRAMES] flag is used to make
        //  frames management automatically.
        //  Other flags would add some useless manual steps
        //  and responsibilities.


        val superName = "java/lang/Object"
        // NOTE: Currently, Drift does not support inheritance from other classes.
        //  So all classes are inherited from `java/lang/Object` until support is added.

        val interfaces = arrayOf<String>()
        // NOTE: Drift does not support interfaces right now.
        //  So an empty array is used to represent no interfaces.


        val name =
            if (namespace.isEmpty()) node.name
            else "$namespace/${node.name}"

        classWriter.visit(
            Emitter.OPCODE_VERSION,
            TempValues.visibility,
            name,
            TempValues.signature,
            superName,
            interfaces)

        val fieldEmitter = FieldEmitter(classWriter)
        val methodEmitter = MethodEmitter(classWriter)

        with(node) {
            (staticFields + fields)
                .forEach(fieldEmitter::emit)

            (staticMethods + methods)
                .forEach(methodEmitter::emit)
        }

        classWriter.visitEnd()

        return classWriter.toByteArray()
    }
}