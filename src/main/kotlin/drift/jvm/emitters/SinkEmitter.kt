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

import drift.hir.HIRNode


/**
 * This emitter contract is a variant of [Emitter]
 * which does not produce value.
 *
 * An example of usage of this emitter variant is
 * for manipulating a [org.objectweb.asm.ClassWriter]
 * or visitor instance.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
interface SinkEmitter<NODE: HIRNode> : Emitter<NODE, Unit>