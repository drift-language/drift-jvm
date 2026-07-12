/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters.types.helpers

import language.Namespace


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
object ClassHelper {

    fun getInternalClassName(namespace: Namespace, clazz: String) =
        if (namespace.getQualifiedName().isEmpty()) clazz
        else "$namespace/${clazz}"

    fun getSyntheticClassName(namespace: Namespace) =
        "$${namespace.getFilename()}"
}