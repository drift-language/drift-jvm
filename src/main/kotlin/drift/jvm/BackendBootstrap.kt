/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm

import drift.hir.HIRClass
import drift.hir.HIRStatement
import drift.jvm.emitters.EmitContext
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.specials.SyntheticClassEmitter
import drift.jvm.emitters.statements.ClassEmitter
import language.Namespace
import java.io.File
import java.io.File.separatorChar


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class BackendBootstrap(
    private val namespace: Namespace,
    private val output: File) {

    private val topLevelStatements = mutableListOf<HIRStatement>()

    private val nodesManager = NodesManager()


    fun boot(hir: List<HIRStatement>) {
        for (node in hir) {
            if (node is HIRClass) handleClass(node)
            else topLevelStatements += node
        }

        handleTopLevelStatements()
    }

    private fun handleClass(hirClass: HIRClass) {
        val byteArray = ClassEmitter(namespace, nodesManager)
            .emit(hirClass)

        println("[BACKEND] [CLASS=${hirClass.name}] Class generation succeeded. " +
                "Emit bytecode length = ${byteArray.contentToString()}.")
    }

    private fun handleTopLevelStatements() {
        val byteArray = SyntheticClassEmitter(namespace, nodesManager)
            .emit(topLevelStatements)

        println("[BACKEND] Top level statements generation succeeded. " +
                "Synthetic class length = ${byteArray.contentToString()}.")

        output.mkdirs()

        val outputFile = output
            .resolve("$${namespace.getFilename()}.class")

        outputFile.writeBytes(byteArray)
    }
}