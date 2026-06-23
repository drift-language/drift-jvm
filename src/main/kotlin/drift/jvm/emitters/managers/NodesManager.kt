/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.managers

import drift.hir.HIRNode


/**
 * Nodes manager permits linking [HIRNode] with definition HIR IDs,
 * and storing top-level variables.
 *
 * @author Jonathan (GitHub: belicfr)
 */
data class NodesManager(
    val nodesByDefinition: MutableMap<Int, HIRNode> = mutableMapOf(),
    val topLevelVariablesDefinitions: MutableSet<Int> = mutableSetOf())