/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements.common

import drift.hir.HIRCapturedVariable
import drift.hir.HIRFunction
import drift.hir.HIRMethod
import drift.hir.HIRParameter
import drift.hir.HIRPrimitiveType
import drift.hir.HIRStatement
import drift.hir.HIRType
import drift.hir.PrimitiveKind
import drift.hir.metadata.HIRAnnotation


@Deprecated("Use parameterFixture() instead")
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


fun parameterFixture(
    hirId: Int = 1,
    name: String = "foo",
    type: HIRType = HIRPrimitiveType(PrimitiveKind.INT)) : HIRParameter {

    return HIRParameter(hirId, name, type)
}


fun methodFixture(
    hirId: Int = 1,
    annotations: MutableList<HIRAnnotation> = mutableListOf(),
    parameters: List<HIRParameter> = emptyList(),
    returnType: HIRType = HIRPrimitiveType(PrimitiveKind.VOID),
    body: List<HIRStatement> = emptyList(),
    name: String = "foo",
    isStatic: Boolean) : HIRMethod {

    return HIRMethod(
        hirId,
        annotations,
        parameters,
        returnType,
        body,
        name,
        isStatic)
}


fun functionFixture(
    hirId: Int = 1,
    annotations: MutableList<HIRAnnotation> = mutableListOf(),
    parameters: List<HIRParameter> = emptyList(),
    returnType: HIRType = HIRPrimitiveType(PrimitiveKind.VOID),
    body: List<HIRStatement> = emptyList(),
    name: String = "foo",
    capturedVariables: List<HIRCapturedVariable> = listOf()) : HIRFunction {

    return HIRFunction(
        hirId,
        annotations,
        parameters,
        returnType,
        body,
        name,
        capturedVariables)
}