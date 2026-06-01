/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters.opcodes


/**
 * Custom opcodes in addition of [org.objectweb.asm.Opcodes].
 * 
 * @author Jonathan (GitHub: belicfr)
 */
object OpcodesPlus {

    /**
     * If a class member is not static.
     *
     * @see org.objectweb.asm.Opcodes.ACC_STATIC
     */
    const val ACC_NOT_STATIC = 0x0000
}