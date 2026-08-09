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

import drift.hir.HirId


/**
 * TODO DOC
 *
 * @author Jonathan (GitHub: belicfr)
 */
class SlotsManager {

    companion object {

        const val SIMPLE_WIDTH = 1
        const val DOUBLE_WIDTH = 2
    }


    var slotIndex = 0
        private set

    private val hirIdToSlotIndex = mutableMapOf<HirId, SlotAllocation>()


    fun getSlotAllocation(hirId: HirId) : SlotAllocation? = hirIdToSlotIndex[hirId]

    fun allocateOneAndLink(hirId: HirId) : SlotAllocation = allocateAndLink(hirId, SIMPLE_WIDTH)

    fun allocateTwoAndLink(hirId: HirId) : SlotAllocation = allocateAndLink(hirId, DOUBLE_WIDTH)

    fun allocateAndLink(hirId: HirId, width: Int) : SlotAllocation {
        val allocation = allocate(width)
        hirIdToSlotIndex[hirId] = allocation

        return allocation
    }

    fun allocate(width: Int) : SlotAllocation {
        val slotToUse = slotIndex
        slotIndex += width

        return SlotAllocation(slotToUse, width)
    }


    data class SlotAllocation(
        val index: Int,
        val width: Int)
}