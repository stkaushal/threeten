/*
 * Copyright (c) 2008, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar.field;

import static org.testng.Assert.*;

import java.io.Serializable;

import javax.time.CalendricalException;
import javax.time.calendar.Calendrical;
import javax.time.calendar.CalendricalProvider;
import javax.time.calendar.DateAdjustor;
import javax.time.calendar.DateMatcher;
import javax.time.calendar.DateProvider;
import javax.time.calendar.DateResolver;
import javax.time.calendar.DateResolvers;
import javax.time.calendar.DateTimeFieldRule;
import javax.time.calendar.ISOChronology;
import javax.time.calendar.IllegalCalendarFieldValueException;
import javax.time.calendar.InvalidCalendarFieldException;
import javax.time.calendar.LocalDate;
import javax.time.calendar.MockDateProviderReturnsNull;
import javax.time.calendar.MockDateResolverReturnsNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test Year.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
@Test
public class TestYear {

    private static final DateTimeFieldRule RULE = ISOChronology.INSTANCE.year();

    @BeforeMethod
    public void setUp() {
    }

    //-----------------------------------------------------------------------
    public void test_interfaces() {
        assertTrue(CalendricalProvider.class.isAssignableFrom(Year.class));
        assertTrue(Serializable.class.isAssignableFrom(Year.class));
        assertTrue(Comparable.class.isAssignableFrom(Year.class));
        assertTrue(DateAdjustor.class.isAssignableFrom(Year.class));
        assertTrue(DateMatcher.class.isAssignableFrom(Year.class));
    }

    //-----------------------------------------------------------------------
    public void test_rule() {
        assertEquals(Year.rule(), RULE);
    }

    //-----------------------------------------------------------------------
    public void test_factory_int_singleton() {
        for (int i = -4; i <= 2104; i++) {
            Year test = Year.isoYear(i);
            assertEquals(test.getValue(), i);
            assertEquals(Year.isoYear(i), test);
        }
    }

    @Test(expectedExceptions=IllegalCalendarFieldValueException.class)
    public void test_factory_int_tooLow() {
        if (Year.MIN_YEAR == Integer.MIN_VALUE) {
            return;
        }
        Year.isoYear(Year.MIN_YEAR - 1);
    }

    @Test(expectedExceptions=IllegalCalendarFieldValueException.class)
    public void test_factory_int_tooHigh() {
        if (Year.MAX_YEAR == Integer.MAX_VALUE) {
            throw new IllegalCalendarFieldValueException("", RULE);
        }
        Year.isoYear(Year.MAX_YEAR + 1);
    }

    //-----------------------------------------------------------------------
    public void test_factory_DateProvider() {
        for (int i = -4; i <= 2104; i++) {  // Jan
            assertEquals(Year.year(LocalDate.date(i, 1, 1)).getValue(), i);
        }
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_factory_nullDateProvider() {
        Year.year((DateProvider) null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_factory_badDateProvider() {
        Year.year(new MockDateProviderReturnsNull());
    }

    //-----------------------------------------------------------------------
    // isLeap()
    //-----------------------------------------------------------------------
    public void test_isLeap() {
        assertEquals(Year.isoYear(1999).isLeap(), false);
        assertEquals(Year.isoYear(2000).isLeap(), true);
        assertEquals(Year.isoYear(2001).isLeap(), false);
        
        assertEquals(Year.isoYear(2007).isLeap(), false);
        assertEquals(Year.isoYear(2008).isLeap(), true);
        assertEquals(Year.isoYear(2009).isLeap(), false);
        assertEquals(Year.isoYear(2010).isLeap(), false);
        assertEquals(Year.isoYear(2011).isLeap(), false);
        assertEquals(Year.isoYear(2012).isLeap(), true);
        
        assertEquals(Year.isoYear(2095).isLeap(), false);
        assertEquals(Year.isoYear(2096).isLeap(), true);
        assertEquals(Year.isoYear(2097).isLeap(), false);
        assertEquals(Year.isoYear(2098).isLeap(), false);
        assertEquals(Year.isoYear(2099).isLeap(), false);
        assertEquals(Year.isoYear(2100).isLeap(), false);
        assertEquals(Year.isoYear(2101).isLeap(), false);
        assertEquals(Year.isoYear(2102).isLeap(), false);
        assertEquals(Year.isoYear(2103).isLeap(), false);
        assertEquals(Year.isoYear(2104).isLeap(), true);
        assertEquals(Year.isoYear(2105).isLeap(), false);
        
        assertEquals(Year.isoYear(-500).isLeap(), false);
        assertEquals(Year.isoYear(-400).isLeap(), true);
        assertEquals(Year.isoYear(-300).isLeap(), false);
        assertEquals(Year.isoYear(-200).isLeap(), false);
        assertEquals(Year.isoYear(-100).isLeap(), false);
        assertEquals(Year.isoYear(0).isLeap(), true);
        assertEquals(Year.isoYear(100).isLeap(), false);
        assertEquals(Year.isoYear(200).isLeap(), false);
        assertEquals(Year.isoYear(300).isLeap(), false);
        assertEquals(Year.isoYear(400).isLeap(), true);
        assertEquals(Year.isoYear(500).isLeap(), false);
    }

    //-----------------------------------------------------------------------
    // next()
    //-----------------------------------------------------------------------
    public void test_next() {
        assertEquals(Year.isoYear(2007).next(), Year.isoYear(2008));
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_next_max() {
        Year.isoYear(Year.MAX_YEAR).next();
    }

    //-----------------------------------------------------------------------
    // nextLeap()
    //-----------------------------------------------------------------------
    public void test_nextLeap() {
        assertEquals(Year.isoYear(2007).nextLeap(), Year.isoYear(2008));
        assertEquals(Year.isoYear(2008).nextLeap(), Year.isoYear(2012));
        assertEquals(Year.isoYear(2009).nextLeap(), Year.isoYear(2012));
        assertEquals(Year.isoYear(2010).nextLeap(), Year.isoYear(2012));
        assertEquals(Year.isoYear(2011).nextLeap(), Year.isoYear(2012));
        assertEquals(Year.isoYear(2012).nextLeap(), Year.isoYear(2016));
        
        assertEquals(Year.isoYear(2096).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2097).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2098).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2099).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2100).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2101).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2102).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2103).nextLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2104).nextLeap(), Year.isoYear(2108));
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_nextLeap_max() {
        Year.isoYear(Year.MAX_YEAR).nextLeap();
    }

    //-----------------------------------------------------------------------
    // previous()
    //-----------------------------------------------------------------------
    public void test_previous() {
        assertEquals(Year.isoYear(2007).previous(), Year.isoYear(2006));
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_previous_min() {
        Year.isoYear(Year.MIN_YEAR).previous();
    }

    //-----------------------------------------------------------------------
    // previousLeap()
    //-----------------------------------------------------------------------
    public void test_previousLeap() {
        assertEquals(Year.isoYear(2013).previousLeap(), Year.isoYear(2012));
        assertEquals(Year.isoYear(2012).previousLeap(), Year.isoYear(2008));
        assertEquals(Year.isoYear(2011).previousLeap(), Year.isoYear(2008));
        assertEquals(Year.isoYear(2010).previousLeap(), Year.isoYear(2008));
        assertEquals(Year.isoYear(2009).previousLeap(), Year.isoYear(2008));
        assertEquals(Year.isoYear(2008).previousLeap(), Year.isoYear(2004));
        assertEquals(Year.isoYear(2007).previousLeap(), Year.isoYear(2004));
        
        assertEquals(Year.isoYear(2105).previousLeap(), Year.isoYear(2104));
        assertEquals(Year.isoYear(2104).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2103).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2102).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2101).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2100).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2099).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2098).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2097).previousLeap(), Year.isoYear(2096));
        assertEquals(Year.isoYear(2096).previousLeap(), Year.isoYear(2092));
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_previousLeap_min() {
        Year.isoYear(Year.MIN_YEAR).previousLeap();
    }

    //-----------------------------------------------------------------------
    // plusYears()
    //-----------------------------------------------------------------------
    public void test_plusYears() {
        assertEquals(Year.isoYear(2007).plusYears(-1), Year.isoYear(2006));
        assertEquals(Year.isoYear(2007).plusYears(0), Year.isoYear(2007));
        assertEquals(Year.isoYear(2007).plusYears(1), Year.isoYear(2008));
        assertEquals(Year.isoYear(2007).plusYears(2), Year.isoYear(2009));
        
        assertEquals(Year.isoYear(Year.MAX_YEAR - 1).plusYears(1), Year.isoYear(Year.MAX_YEAR));
        assertEquals(Year.isoYear(Year.MAX_YEAR).plusYears(0), Year.isoYear(Year.MAX_YEAR));
        
        assertEquals(Year.isoYear(Year.MIN_YEAR + 1).plusYears(-1), Year.isoYear(Year.MIN_YEAR));
        assertEquals(Year.isoYear(Year.MIN_YEAR).plusYears(0), Year.isoYear(Year.MIN_YEAR));
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_plusYears_max() {
        Year.isoYear(Year.MAX_YEAR).plusYears(1);
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_plusYears_maxLots() {
        Year.isoYear(Year.MAX_YEAR).plusYears(1000);
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_plusYears_min() {
        Year.isoYear(Year.MIN_YEAR).plusYears(-1);
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_plusYears_minLots() {
        Year.isoYear(Year.MIN_YEAR).plusYears(-1000);
    }

    //-----------------------------------------------------------------------
    // minusYears()
    //-----------------------------------------------------------------------
    public void test_minusYears() {
        assertEquals(Year.isoYear(2007).minusYears(-1), Year.isoYear(2008));
        assertEquals(Year.isoYear(2007).minusYears(0), Year.isoYear(2007));
        assertEquals(Year.isoYear(2007).minusYears(1), Year.isoYear(2006));
        assertEquals(Year.isoYear(2007).minusYears(2), Year.isoYear(2005));
        
        assertEquals(Year.isoYear(Year.MAX_YEAR - 1).minusYears(-1), Year.isoYear(Year.MAX_YEAR));
        assertEquals(Year.isoYear(Year.MAX_YEAR).minusYears(0), Year.isoYear(Year.MAX_YEAR));
        
        assertEquals(Year.isoYear(Year.MIN_YEAR + 1).minusYears(1), Year.isoYear(Year.MIN_YEAR));
        assertEquals(Year.isoYear(Year.MIN_YEAR).minusYears(0), Year.isoYear(Year.MIN_YEAR));
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_minusYears_max() {
        Year.isoYear(Year.MAX_YEAR).minusYears(-1);
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_minusYears_maxLots() {
        Year.isoYear(Year.MAX_YEAR).minusYears(-1000);
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_minusYears_min() {
        Year.isoYear(Year.MIN_YEAR).minusYears(1);
    }

    @Test(expectedExceptions=CalendricalException.class)
    public void test_minusYears_minLots() {
        Year.isoYear(Year.MIN_YEAR).minusYears(1000);
    }

    //-----------------------------------------------------------------------
    // adjustDate(LocalDate)
    //-----------------------------------------------------------------------
    public void test_adjustDate() {
        LocalDate base = LocalDate.date(2007, 2, 12);
        for (int i = -4; i <= 2104; i++) {
            LocalDate result = Year.isoYear(i).adjustDate(base);
            assertEquals(result, LocalDate.date(i, 2, 12));
        }
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_adjustDate_nullLocalDate() {
        Year test = Year.isoYear(1);
        test.adjustDate((LocalDate) null);
    }

    //-----------------------------------------------------------------------
    // adjustDate(LocalDate,DateResolver)
    //-----------------------------------------------------------------------
    public void test_adjustDate_strictResolver() {
        LocalDate base = LocalDate.date(2007, 2, 12);
        for (int i = -4; i <= 2104; i++) {
            LocalDate result = Year.isoYear(i).adjustDate(base, DateResolvers.strict());
            assertEquals(result, LocalDate.date(i, 2, 12));
        }
    }

    @Test(expectedExceptions=InvalidCalendarFieldException.class)
    public void test_adjustDate_strictResolver_feb29() {
        LocalDate base = LocalDate.date(2008, 2, 29);
        Year test = Year.isoYear(2007);
        try {
            test.adjustDate(base, DateResolvers.strict());
        } catch (InvalidCalendarFieldException ex) {
            assertEquals(ex.getFieldRule(), ISOChronology.INSTANCE.dayOfMonth());
            throw ex;
        }
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_adjustDate_resolver_nullLocalDate() {
        Year test = Year.isoYear(1);
        test.adjustDate((LocalDate) null, DateResolvers.strict());
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_adjustDate_resolver_nullResolver() {
        LocalDate date = LocalDate.date(2007, 1, 1);
        Year test = Year.isoYear(1);
        test.adjustDate(date, (DateResolver) null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_adjustDate_resolver_badResolver() {
        LocalDate date = LocalDate.date(2007, 1, 31);
        Year test = Year.isoYear(2);
        test.adjustDate(date, new MockDateResolverReturnsNull());
    }

    //-----------------------------------------------------------------------
    // matchesDate(LocalDate)
    //-----------------------------------------------------------------------
    public void test_matchesDate_notLeapYear() {
        LocalDate work = LocalDate.date(2007, 3, 2);
        for (int i = -4; i <= 2104; i++) {
            for (int j = -4; j <= 2104; j++) {
                Year test = Year.isoYear(j);
                assertEquals(test.matchesDate(work), work.getYear().getValue() == j);
            }
            work = work.plusYears(1);
        }
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_matchesDate_nullLocalDate() {
        Year test = Year.isoYear(1);
        test.matchesDate((LocalDate) null);
    }

//    //-----------------------------------------------------------------------
//    // getEstimatedEra()
//    //-----------------------------------------------------------------------
//    public void test_getEstimatedEra() {
//        assertEquals(Year.isoYear(2).getEstimatedEra(), Era.AD);
//        assertEquals(Year.isoYear(1).getEstimatedEra(), Era.AD);
//        assertEquals(Year.isoYear(0).getEstimatedEra(), Era.BC);
//        assertEquals(Year.isoYear(-1).getEstimatedEra(), Era.BC);
//    }
//
//    //-----------------------------------------------------------------------
//    // getYearOfEstimatedEra()
//    //-----------------------------------------------------------------------
//    public void test_getYearOfEstimatedEra() {
//        assertEquals(Year.isoYear(2).getYearOfEstimatedEra(), 2);
//        assertEquals(Year.isoYear(1).getYearOfEstimatedEra(), 1);
//        assertEquals(Year.isoYear(0).getYearOfEstimatedEra(), 1);
//        assertEquals(Year.isoYear(-1).getYearOfEstimatedEra(), 2);
//    }
//
//    //-----------------------------------------------------------------------
//    // getISOCentury()
//    //-----------------------------------------------------------------------
//    public void test_getISOCentury() {
//        assertEquals(Year.isoYear(2008).getISOCentury(), 20);
//        assertEquals(Year.isoYear(101).getISOCentury(), 1);
//        assertEquals(Year.isoYear(100).getISOCentury(), 1);
//        assertEquals(Year.isoYear(99).getISOCentury(), 0);
//        assertEquals(Year.isoYear(1).getISOCentury(), 0);
//        assertEquals(Year.isoYear(0).getISOCentury(), 0);
//        assertEquals(Year.isoYear(-1).getISOCentury(), 0);
//        assertEquals(Year.isoYear(-99).getISOCentury(), 0);
//        assertEquals(Year.isoYear(-100).getISOCentury(), -1);
//        assertEquals(Year.isoYear(-101).getISOCentury(), -1);
//    }
//
//    //-----------------------------------------------------------------------
//    // getYearOfISOCentury()
//    //-----------------------------------------------------------------------
//    public void test_getYearOfISOCentury() {
//        assertEquals(Year.isoYear(2008).getYearOfISOCentury(), 8);
//        assertEquals(Year.isoYear(101).getYearOfISOCentury(), 1);
//        assertEquals(Year.isoYear(100).getYearOfISOCentury(), 0);
//        assertEquals(Year.isoYear(99).getYearOfISOCentury(), 99);
//        assertEquals(Year.isoYear(1).getYearOfISOCentury(), 1);
//        assertEquals(Year.isoYear(0).getYearOfISOCentury(), 0);
//        assertEquals(Year.isoYear(-1).getYearOfISOCentury(), 1);
//        assertEquals(Year.isoYear(-99).getYearOfISOCentury(), 99);
//        assertEquals(Year.isoYear(-100).getYearOfISOCentury(), 0);
//        assertEquals(Year.isoYear(-101).getYearOfISOCentury(), 1);
//    }

    //-----------------------------------------------------------------------
    // toCalendrical()
    //-----------------------------------------------------------------------
    public void test_toCalendrical() {
        for (int i = -4; i <= 2104; i++) {
            Year test = Year.isoYear(i);
            assertEquals(test.toCalendrical(), Calendrical.calendrical(RULE, i));
        }
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    public void test_compareTo() {
        for (int i = -4; i <= 2104; i++) {
            Year a = Year.isoYear(i);
            for (int j = -4; j <= 2104; j++) {
                Year b = Year.isoYear(j);
                if (i < j) {
                    assertEquals(a.compareTo(b), -1);
                    assertEquals(b.compareTo(a), 1);
                } else if (i > j) {
                    assertEquals(a.compareTo(b), 1);
                    assertEquals(b.compareTo(a), -1);
                } else {
                    assertEquals(a.compareTo(b), 0);
                    assertEquals(b.compareTo(a), 0);
                }
            }
        }
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void test_compareTo_nullYear() {
        Year doy = null;
        Year test = Year.isoYear(1);
        test.compareTo(doy);
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode()
    //-----------------------------------------------------------------------
    public void test_equals() {
        for (int i = -4; i <= 2104; i++) {
            Year a = Year.isoYear(i);
            for (int j = -4; j <= 2104; j++) {
                Year b = Year.isoYear(j);
                assertEquals(a.equals(b), i == j);
                assertEquals(a.hashCode() == b.hashCode(), i == j);
            }
        }
    }

    public void test_equals_nullYear() {
        Year doy = null;
        Year test = Year.isoYear(1);
        assertEquals(test.equals(doy), false);
    }

    public void test_equals_incorrectType() {
        Year test = Year.isoYear(1);
        assertEquals(test.equals("Incorrect type"), false);
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    public void test_toString() {
        for (int i = -4; i <= 2104; i++) {
            Year a = Year.isoYear(i);
            assertEquals(a.toString(), "Year=" + i);
        }
    }

}