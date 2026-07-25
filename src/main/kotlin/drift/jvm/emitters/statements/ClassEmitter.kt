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
import drift.jvm.emitters.common.ClassGenerator
import drift.jvm.emitters.common.ClassGenerator.ClassMembers
import drift.jvm.emitters.managers.NodesManager
import language.Namespace


/**
 * This emitter class permits emitting [HIRClass] nodes to [ByteArray].
 *
 * All Drift classes are emitted to bytecode which can then be written
 * into a `.class` file and run on JVM.
 *
 * @author Jonathan (GitHub: belicfr)
 */
class ClassEmitter(
    private val namespace: Namespace,
    private val nodesManager: NodesManager)
    : Emitter<HIRClass, ByteArray> {

    override fun emit(node: HIRClass) : ByteArray {
        val members = ClassMembers(
            fields = node.fields,
            staticFields = node.staticFields,
            methods = node.methods,
            staticMethods = node.staticMethods)

        val className = (namespace + node.name).getNamespace()
        val classWriter = ClassGenerator(namespace, nodesManager)
            .generate(className, members)

        classWriter.visitEnd()

        nodesManager.nodesByDefinition[node.hirId] = node

        return classWriter.toByteArray()
    }
}