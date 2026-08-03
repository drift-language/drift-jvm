/******************************************************************************
 * Drift Programming Language                                                 *
 *                                                                            *
 * Copyright (c) 2026. Jonathan (GitHub: belicfr)                             *
 *                                                                            *
 * This source code is licensed under the MIT License.                        *
 * See the LICENSE file in the root directory for details.                    *
 ******************************************************************************/
package drift.jvm.emitters.statements.common

import drift.hir.HIRPrimitiveType
import drift.hir.PrimitiveKind
import drift.jvm.emitters.managers.SlotsManager
import drift.jvm.emitters.managers.SlotsManager.Companion.DOUBLE_WIDTH
import drift.jvm.emitters.managers.SlotsManager.Companion.SIMPLE_WIDTH
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CallableEmittersCommonTest {

    @Nested
    inner class AllocateCallableParameterSlot {

        private val hirId = 1

        private lateinit var slotsManager: SlotsManager


        @BeforeEach
        fun setUp() {
            slotsManager = SlotsManager()
        }


        @Test
        fun `Simple width parameter must allocate a slot of width 1`() {
            // GIVEN
            val parameter = HIRParameterUtils
                .createWithType(hirId, HIRPrimitiveType(PrimitiveKind.INT))


            // WHEN
            val slot = allocateCallableParameterSlot(slotsManager, parameter)

            // THEN
            slot.width shouldBe SIMPLE_WIDTH
        }

        @Test
        fun `Double width parameter must allocate a slot of width 2`() {
            // GIVEN
            val parameter = HIRParameterUtils
                .createWithType(hirId, HIRPrimitiveType(PrimitiveKind.INT64))


            // WHEN
            val slot = allocateCallableParameterSlot(slotsManager, parameter)

            // THEN
            slot.width shouldBe DOUBLE_WIDTH
        }

        @Test
        fun `Allocation must respect index increment`() {
            // GIVEN
            slotsManager.allocateOneAndLink(-1)
            // NOTE: allocates a slot of width 1.

            val parameter = HIRParameterUtils
                .createWithType(hirId, HIRPrimitiveType(PrimitiveKind.INT))

            val expectedSlotIndex = SIMPLE_WIDTH    // 0 + (1)

            // WHEN
            val slot = allocateCallableParameterSlot(slotsManager, parameter)

            // THEN
            slot.index shouldBe expectedSlotIndex
        }
    }


    @Nested
    inner class AllocateCallableParameterSlots {

        //
    }


    @Nested
    inner class EmitCallableBody {

        //
    }
}