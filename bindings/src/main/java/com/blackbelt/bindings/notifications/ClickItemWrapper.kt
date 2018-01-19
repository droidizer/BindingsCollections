package com.blackbelt.bindings.notifications

class ClickItemWrapper private constructor() {

    var clickedItemId: Int = 0
        private set

    var additionalData: Any? = null
        private set

    companion object {

        fun <T> withAdditionalData(id: Int, additionalData: T): ClickItemWrapper {
            val clickItemWrapper = ClickItemWrapper()
            clickItemWrapper.additionalData = additionalData
            clickItemWrapper.clickedItemId = id
            return clickItemWrapper
        }

        fun simpleClickItemWrapper(id: Int): ClickItemWrapper {
            val clickItemWrapper = ClickItemWrapper()
            clickItemWrapper.clickedItemId = id
            return clickItemWrapper
        }
    }
}