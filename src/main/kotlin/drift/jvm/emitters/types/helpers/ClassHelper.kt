/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.types.helpers


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
object ClassHelper {

    fun getInternalClassName(namespace: String, clazz: String) =
        if (namespace.isEmpty()) clazz
        else "$namespace/${clazz}"
}