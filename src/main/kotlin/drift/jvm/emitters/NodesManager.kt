/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters

import drift.hir.HIRNode


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class NodesManager {

    private val nodeByHirId = mutableMapOf<Int, HIRNode>()


    fun get(hirId: Int) = nodeByHirId[hirId]

    fun has(hirId: Int) = get(hirId) != null

    fun set(hirId: Int, node: HIRNode) {
        nodeByHirId[hirId] = node
    }

    fun remove(hirId: Int) {
        if (!has(hirId))
            error("Unknown node registration")

        nodeByHirId.remove(hirId)
    }
}