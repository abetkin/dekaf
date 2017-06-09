package org.jetbrains.dekaf.impl

import org.jetbrains.dekaf.assertions.IsFalse
import org.jetbrains.dekaf.assertions.IsNotNull
import org.jetbrains.dekaf.assertions.IsTrue
import org.jetbrains.dekaf.assertions.expected
import org.jetbrains.dekaf.core.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


class LayoutsTest {

    companion object {

        @JvmStatic
        internal val provider = BaseProvider()

        @JvmStatic
        internal val db = provider.provide("jdbc:h2:mem:Testing")

        @BeforeAll @JvmStatic
        fun connectToH2() {
            db.connect()
        }

    }

    @Test
    fun existence_0() {
        val layout = layoutExistence()
        val result: Boolean? = db.inTransaction { tran ->
            tran.query("select 0 where 1 is null", layout).run()
        }

        result expected IsNotNull
        result expected IsFalse
    }

    @Test
    fun existence_1() {
        val layout = layoutExistence()
        val result: Boolean? = db.inTransaction { tran ->
            tran.query("select 1", layout).run()
        }

        result expected IsNotNull
        result expected IsTrue
    }


    @Test
    fun singleValue() {
        val layout = layoutSingleValueOf<Number>()
        val queryText = "select 1234 union all select 5678 order by 1"
        val result: Number? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected 1234
    }


    @Test
    fun arrayOfShort_5() {
        val layout = layoutArrayOfShort()
        val queryText = "select * from table (nr smallint=(11,22,33,44,55)) order by 1"
        val result: ShortArray? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!
        result expected shortArrayOf(11,22,33,44,55)
    }

    @Test
    fun arrayOfShort_5000() {
        val queryText = """|select T * 1000 + X * 100 + Y * 10 + Z + 1 as nr
                           |from table(T smallint = (0,1,2,3,4))
                           |     cross join
                           |     table(X smallint = (0,1,2,3,4,5,6,7,8,9))
                           |     cross join
                           |     table(Y smallint = (0,1,2,3,4,5,6,7,8,9))
                           |     cross join
                           |     table(Z smallint = (0,1,2,3,4,5,6,7,8,9))
                           |order by nr
                        """.trimMargin()

        val layout = layoutArrayOfShort()
        val result: ShortArray? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!.size expected 5000

        val expectedArray = ShortArray(5000) {(it+1).toShort()}
        result expected expectedArray
    }


    @Test
    fun arrayOfInt_5() {
        val layout = layoutArrayOfInt()
        val queryText = "select * from table (nr int=(11,22,33,44,55)) order by 1"
        val result: IntArray? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!
        result expected intArrayOf(11,22,33,44,55)
    }

    @Test
    fun arrayOfInt_5000() {
        val queryText = """|select T * 1000 + X * 100 + Y * 10 + Z + 1 as nr
                           |from table(T int = (0,1,2,3,4))
                           |     cross join
                           |     table(X int = (0,1,2,3,4,5,6,7,8,9))
                           |     cross join
                           |     table(Y int = (0,1,2,3,4,5,6,7,8,9))
                           |     cross join
                           |     table(Z int = (0,1,2,3,4,5,6,7,8,9))
                           |order by nr
                        """.trimMargin()

        val layout = layoutArrayOfInt()
        val result: IntArray? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!.size expected 5000

        val expectedArray = IntArray(5000) {it+1}
        result expected expectedArray
    }


    @Test
    fun arrayOfLong_5() {
        val layout = layoutArrayOfLong()
        val queryText = "select * from table (nr long=(11,22,33,44,55)) order by 1"
        val result: LongArray? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!
        result expected longArrayOf(11,22,33,44,55)
    }

    @Test
    fun arrayOfLong_5000() {
        val queryText = """|select T * 1000 + X * 100 + Y * 10 + Z + 1 as nr
                           |from table(T long = (0,1,2,3,4))
                           |     cross join
                           |     table(X long = (0,1,2,3,4,5,6,7,8,9))
                           |     cross join
                           |     table(Y long = (0,1,2,3,4,5,6,7,8,9))
                           |     cross join
                           |     table(Z long = (0,1,2,3,4,5,6,7,8,9))
                           |order by nr
                        """.trimMargin()

        val layout = layoutArrayOfLong()
        val result: LongArray? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!.size expected 5000

        val expectedArray = LongArray(5000) {it+1L}
        result expected expectedArray
    }


    @Test
    fun arrayOfNumber() {
        val layout = layoutArrayOf(rowValueOf<Number>())
        val queryText = "select * from table (nr int=(1234,5678,9012)) order by 1"
        val result: Array<out Number>? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!
        result expected arrayOf<Number>(1234,5678,9012)
    }

    @Test
    fun listOfNumber() {
        val layout = layoutListOf(rowValueOf<Number>())
        val queryText = "select * from table (nr int=(1234,5678,9012)) order by 1"
        val result: List<Number>? = db.inTransaction { tran ->
            tran.query(queryText, layout).run()
        }

        result expected IsNotNull
        result!!
        result expected listOf<Number>(1234,5678,9012)
    }

}