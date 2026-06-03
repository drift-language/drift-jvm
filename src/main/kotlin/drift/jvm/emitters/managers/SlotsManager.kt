/*
 * Drift Programming Language
 * Drift JVM Backend
 *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the root directory for details.
 */

package drift.jvm.emitters.managers


/**
 * Slots manager permits linking slot indexes with HIR IDs.
 * It is useful to handle variable references.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
class SlotsManager : Manager<Int, Int>()