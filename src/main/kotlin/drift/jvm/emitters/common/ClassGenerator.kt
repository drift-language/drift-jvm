/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.common

import drift.hir.HIRField
import drift.hir.HIRFunction
import drift.jvm.emitters.Emitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.statements.FieldEmitter
import drift.jvm.emitters.statements.MethodEmitter
import drift.jvm.emitters.types.helpers.ClassHelper.getInternalClassName
import language.Namespace
import org.objectweb.asm.ClassWriter


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class ClassGenerator(
    val namespace: Namespace) {

    fun generate(name: String, members: ClassMembers) : ClassWriter {
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


        val name = getInternalClassName(namespace, name)

        classWriter.visit(
            Emitter.OPCODE_VERSION,
            TempValues.visibility,
            name,
            TempValues.signature,
            superName,
            interfaces)

        val fieldEmitter = FieldEmitter(classWriter)
        val methodEmitter = MethodEmitter(classWriter)

        with(members) {
            (staticFields + fields)
                .forEach(fieldEmitter::emit)

            (staticMethods + methods)
                .forEach(methodEmitter::emit)
        }

        return classWriter
    }


    data class ClassMembers(
        val fields: List<HIRField> = emptyList(),
        val staticFields: List<HIRField> = emptyList(),
        val methods: List<HIRFunction> = emptyList(),
        val staticMethods: List<HIRFunction> = emptyList())
}