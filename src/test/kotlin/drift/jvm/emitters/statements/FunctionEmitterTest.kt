/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements

import drift.hir.HIRPrimitiveType
import drift.hir.PrimitiveKind
import drift.jvm.emitters.TempValues
import drift.jvm.emitters.managers.NodesManager
import drift.jvm.emitters.statements.common.functionFixture
import drift.jvm.emitters.statements.common.methodFixture
import drift.jvm.emitters.statements.common.parameterFixture
import drift.jvm.emitters.sugar.then
import drift.jvm.emitters.types.helpers.TypeConverter.formatTypes
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifyOrder
import language.Namespace
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ACC_STATIC

class FunctionEmitterTest {

    private val namespace = mockk<Namespace>(relaxed = true)
    private val nodesManager = mockk<NodesManager>(relaxed = true)
    private val classWriter = mockk<ClassWriter>(relaxed = true)
    private val methodVisitor = mockk<MethodVisitor>(relaxed = true)


    @Nested
    inner class AccessFlag {

        @Test
        fun `Access flag must always be PUBLIC STATIC`() {
            // NOTE: cf. FunctionEmitter access flag notes.

            // GIVEN
            val node = functionFixture()
            val accessFlag = slot<Int>()

            every {
                classWriter.visitMethod(
                    capture(accessFlag),
                    any(),
                    any(),
                    any(),
                    any())
            } returns methodVisitor

            // WHEN
            FunctionEmitter(namespace, classWriter, nodesManager)
                .emit(node)

            // THEN
            with(accessFlag) {
                isCaptured.shouldBeTrue()
                captured shouldBe (TempValues.visibility then ACC_STATIC)
            }
        }
    }


    @Nested
    inner class MethodVisit {
        // NOTE: access flag is tested in AccessFlag section.

        @Test
        fun `visitMethod must be called on emit with expected values`() {
            // GIVEN
            val parameter = parameterFixture()
            val node = functionFixture(
                parameters = listOf(parameter),
                returnType = HIRPrimitiveType(PrimitiveKind.INT64))
            val name = slot<String>()
            val descriptor = slot<String>()
            val signature = slot<String?>()
            val exceptions = slot<Array<String>>()

            val expectedDescriptor = formatTypes(
                input = listOf(parameter.type),
                output = node.returnType)

            every {
                classWriter.visitMethod(
                    any(),
                    capture(name),
                    capture(descriptor),
                    captureNullable(signature),
                    capture(exceptions))
            } returns methodVisitor

            // WHEN
            FunctionEmitter(namespace, classWriter, nodesManager)
                .emit(node)

            // THEN
            assertSoftly {
                name.isCaptured.shouldBeTrue()
                descriptor.isCaptured.shouldBeTrue()
                signature.isCaptured.shouldBeTrue()
                exceptions.isCaptured.shouldBeTrue()
            }

            name.captured shouldBe node.name
            descriptor.captured shouldBe expectedDescriptor
            signature.captured shouldBe TempValues.signature
            exceptions.captured shouldBe TempValues.exceptions
        }
    }


    @Nested
    inner class BodyEmission {

        @Test
        fun `Body emission must have control statements in the good order`() {
            // GIVEN
            val node = functionFixture()

            every {
                classWriter.visitMethod(
                    any(),
                    any(),
                    any(),
                    any(),
                    any())
            } returns methodVisitor

            // WHEN
            FunctionEmitter(namespace, classWriter, nodesManager)
                .emit(node)

            // THEN
            verifyOrder {
                methodVisitor.visitCode()
                methodVisitor.visitMaxs(0, 0)
                methodVisitor.visitEnd()
            }
        }
    }
}