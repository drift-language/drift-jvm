/******************************************************************************
 * Drift Programming Language                                                 *
 * Drift Backend Development: Java Virtual Machine implementation.            *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/

package drift.jvm.emitters

import drift.hir.HIRNode


/**
 *
 * 
 * @author Jonathan (GitHub: belicfr)
 */
interface PluralEmitter<NODE_COLLECTION : Collection<HIRNode>, R> {

    fun emit(nodes : NODE_COLLECTION) : R
}