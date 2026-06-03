/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.managers

import drift.hir.HIRNode


/**
 * Nodes manager permits linking [HIRNode] with HIR IDs.
 * It is useful to handle node references.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class NodesManager : Manager<Int, HIRNode>()