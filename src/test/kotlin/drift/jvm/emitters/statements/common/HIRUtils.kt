/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements.common

import drift.hir.HIRParameter
import drift.hir.HIRType


object HIRParameterUtils {

    /**
     * Creates a new instance of [HIRParameter] with a stub name and the
     * provided [hirId] and [type].
     *
     * @return A new instance of [HIRParameter] with the provided type.
     */
    fun createWithType(hirId: Int, type: HIRType) : HIRParameter {
        return HIRParameter(
            hirId,
            name = "foo",
            type)
    }
}