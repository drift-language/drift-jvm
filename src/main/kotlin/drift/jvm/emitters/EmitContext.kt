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

import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.managers.SlotsManager
import org.objectweb.asm.MethodVisitor


/**
 * Context provided on each [Emitter] which needs
 * the current [MethodVisitor] and [drift.jvm.emitters.managers.SlotsManager] instances.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
data class EmitContext(
    val methodVisitor: MethodVisitor,
    val slotsManager: SlotsManager,
    val nodesManager: NodesManager
)
