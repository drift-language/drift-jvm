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

import org.objectweb.asm.MethodVisitor


/**
 * Context provided on each [Emitter] which needs
 * the current [MethodVisitor] and [SlotsManager] instances.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
data class EmitContext(
    val methodVisitor: MethodVisitor,
    val slotsManager: SlotsManager)
