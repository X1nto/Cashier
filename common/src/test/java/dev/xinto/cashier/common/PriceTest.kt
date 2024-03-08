package dev.xinto.cashier.common

import dev.xinto.cashier.common.domain.model.Price
import kotlin.test.Test
import kotlin.test.assertEquals

class PriceTest {

    @Test
    fun testFromString() {
        assertEquals(Price(0), Price.fromString("0"))
        assertEquals(Price(0), Price.fromString("0.00"))
        assertEquals(Price(1), Price.fromString("0.01"))
        assertEquals(Price(10), Price.fromString("0.10"))
        assertEquals(Price(10), Price.fromString(".10"))
        assertEquals(Price(5000), Price.fromString("50"))
        assertEquals(Price(5010), Price.fromString("50.1"))
        assertEquals(Price(5015), Price.fromString("50.15"))
        assertEquals(null, Price.fromString("50.100"))
        assertEquals(null, Price.fromString(""))
    }

    @Test
    fun testToString() {
        assertEquals("50.00GEL", Price(5000).toString())
        assertEquals("50.10GEL", Price(5010).toString())
        assertEquals("50.15GEL", Price(5015).toString())
        assertEquals("0.15GEL", Price(15).toString())
        assertEquals("0.10GEL", Price(10).toString())
        assertEquals("0.01GEL", Price(1).toString())
    }

}