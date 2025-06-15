package com.dentmanage.common

abstract class Value<T : Any>(val value: T) {
    override fun equals(other: Any?) = javaClass == other?.javaClass && value == (other as Value<*>).value
    override fun hashCode() = value.hashCode()
    override fun toString() = value.toString()
}

// Probably Value<Value> not Value<String>
abstract class StringValue(value: String) : Value<String>(value), CharSequence by value, Comparable<StringValue> {
    override fun compareTo(other: StringValue) = value.compareTo(other.value)
    companion object {
        fun StringValue?.equals(other: StringValue?, ignoreCase: Boolean = false): Boolean =
            this?.value.equals(other?.value, ignoreCase = ignoreCase)
    }
}

class UserCode(value: String) : StringValue(value) {
    companion object {
        const val PREFIX = "DU"

        fun fromSequenceValue(sequenceValue: Int): UserCode {
//            require(sequenceValue in 1 .. 999_999_999)
            return UserCode(PREFIX + sequenceValue.toString().padStart(9, '0'))
        }
    }
}