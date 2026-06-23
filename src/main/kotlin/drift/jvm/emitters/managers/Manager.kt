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


/**
 * A manager permits linking and storing elements
 * like nodes with basic action methods.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
@Deprecated("Useless, must be replaced by a direct mutable map")
abstract class Manager<K, V> {

    private val map = mutableMapOf<K, V>()


    fun get(key: K) = map[key]

    fun has(key: K) = get(key) != null

    fun set(key: K, value: V) {
        map[key] = value
    }

    fun remove(key: K) {
        if (!has(key))
            error("Unknown registration")

        map.remove(key)
    }
}