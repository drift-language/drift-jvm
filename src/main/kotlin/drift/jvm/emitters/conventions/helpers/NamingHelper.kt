/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.conventions.helpers


/**
 * Utility object offering multiple tools
 * to generalize naming conventions.
 * 
 * @author Jonathan (GitHub: belicfr)
 */
object NamingHelper {

    /**
     * Construct an internal class name string.
     *
     * Format:  ``com/example/$HIRID$Foo``
     *
     * @return Internal class name string
     */
    fun formatClassName(namespace: String, hirId: Int, name: String) =
        "$namespace/$$hirId$$name"
}