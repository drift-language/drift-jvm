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
import drift.hir.HIRMethod
import drift.hir.HIRStatement
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.Emitter
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.statements.FieldEmitter
import drift.jvm.emitters.statements.MethodEmitter
import drift.jvm.emitters.statements.StatementEmitter
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.ClassHelper.getInternalClassName
import language.Namespace
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.Opcodes.RETURN


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class ClassGenerator(
    private val namespace: Namespace,
    private val nodesManager: NodesManager) {

    fun generate(name: String, members: ClassMembers) =
        generate(name, members, emptyList())

    fun generate(
        name: String,
        members: ClassMembers,
        executableStatements: Collection<HIRStatement>)
        : ClassWriter {

        val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        // NOTE: [ClassWriter.COMPUTE_FRAMES] flag is used to make
        //  frames management automatically.
        //  Other flags would add some useless manual steps
        //  and responsibilities.


        val superName = "java/lang/Object"
        // NOTE: Currently, Drift does not support inheritance from other
        //  classes. So all classes are inherited from `java/lang/Object` until
        //  support is added.

        val interfaces = arrayOf<String>()
        // NOTE: Drift does not support interfaces right now.
        //  So an empty array is used to represent no interfaces.


        classWriter.visit(
            Emitter.OPCODE_VERSION,
            TempValues.visibility,
            name,
            TempValues.signature,
            superName,
            interfaces)

        val fieldEmitter = FieldEmitter(classWriter)
        val methodEmitter = MethodEmitter(
            namespace,
            classWriter,
            nodesManager)

        with(members) {
            (staticFields + fields)
                .forEach(fieldEmitter::emit)

            (staticMethods + methods)
                .forEach(methodEmitter::emit)
        }

        generateMainMethod(classWriter, executableStatements)

        return classWriter
    }

    private fun generateMainMethod(
        classWriter: ClassWriter,
        executableStatements: Collection<HIRStatement>) {

        val access = ACC_PUBLIC then ACC_STATIC
        val name = "main"
        val descriptor = "([Ljava/lang/String;)V"

        val mainVisitor = classWriter.visitMethod(
            access,
            name,
            descriptor,
            null,
            null)

        mainVisitor.visitCode()

        val context = EmitContext(
            mainVisitor,
            SlotsManager(),
            nodesManager)

        val stmtEmitter = StatementEmitter(namespace, context)

        executableStatements.forEach(stmtEmitter::emit)

        mainVisitor.visitInsn(RETURN)
        mainVisitor.visitEnd()
    }


    data class ClassMembers(
        val fields: List<HIRField> = emptyList(),
        val staticFields: List<HIRField> = emptyList(),
        val methods: List<HIRMethod> = emptyList(),
        val staticMethods: List<HIRMethod> = emptyList())
}