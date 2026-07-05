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

import drift.hir.HIRFunction
import drift.hir.HIRStatement
import drift.hir.HIRVariable
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.PluralEmitter
import drift.jvm.emitters.common.ClassGenerator
import drift.jvm.emitters.common.ClassGenerator.ClassMembers
import drift.jvm.emitters.expressions.ExpressionEmitter
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.types.helpers.TypeConverter.toAsmType
import language.Namespace
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.Opcodes.PUTSTATIC
import org.objectweb.asm.Opcodes.RETURN


/**
 * This emitter class permits emitting top-level statements by generating
 * a synthetic class, allowing functional programming on JVM.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class SyntheticClassEmitter(
    private val namespace: Namespace,
    private val nodesManager: NodesManager)
    : PluralEmitter<List<HIRStatement>, ByteArray> {

    override fun emit(nodes: List<HIRStatement>) : ByteArray {
        val className = "$${namespace.getFilename()}"

        println("Emitting synthetic class... '${className}'")

        val variables = nodes
            .filterIsInstance<HIRVariable>()

        val functions = nodes
            .filterIsInstance<HIRFunction>()

        val members = ClassMembers(
            staticMethods = functions)

        val executableStatements = nodes
            .filter { it !is HIRFunction && it !is HIRVariable }

        val classWriter = ClassGenerator(namespace, nodesManager)
            .generate(className, members, executableStatements)

        val tlVarEmitter = TopLevelVariableEmitter(classWriter)

        val clinitVisitor = classWriter.visitMethod(
            ACC_STATIC,
            "<clinit>",
            "()V",
            null,
            null)

        clinitVisitor.visitCode()

        val context = EmitContext(
            methodVisitor = clinitVisitor,
            slotsManager = SlotsManager(),
            nodesManager = nodesManager)

        val expressionEmitter = ExpressionEmitter(namespace, context)

        variables.forEach { variable ->
            with(nodesManager) {
                nodesByDefinition[variable.hirId] = variable
            }

            tlVarEmitter.emit(variable)

            variable.initialValue?.let { initialValue ->
                expressionEmitter
                    .emit(initialValue)

                clinitVisitor.visitFieldInsn(
                    PUTSTATIC,
                    className,
                    variable.name,
                    toAsmType(variable.type))
            }
        }

        clinitVisitor.visitInsn(RETURN)
        clinitVisitor.visitEnd()

        classWriter.visitEnd()

        return classWriter.toByteArray()
    }
}