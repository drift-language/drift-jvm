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

    private val hirIdToSlotIndex = mutableMapOf<Int, Int>()


    fun getSlotIndex(hirId: Int) : Int? = hirIdToSlotIndex[hirId]

    fun allocateOneAndLink(hirId: Int) : Int = allocateAndLink(hirId, SIMPLE_WIDTH)

    fun allocateTwoAndLink(hirId: Int) : Int = allocateAndLink(hirId, DOUBLE_WIDTH)

    fun allocateAndLink(hirId: Int, width: Int) : Int {
        val slotToUse = allocate(width)
        hirIdToSlotIndex[hirId] = slotToUse

        return slotToUse
    }

    fun allocate(width: Int) : Int {
        val slotToUse = slotIndex
        slotIndex += width

        return slotToUse
    }
}