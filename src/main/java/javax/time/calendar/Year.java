/*
 * Copyright (c) 2007, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.calendar;

import java.io.Serializable;

import javax.time.MathUtils;
import javax.time.calendar.field.Era;
import javax.time.period.PeriodView;
import javax.time.period.Periods;

/**
 * A calendrical representation of a year without a time zone.
 * <p>
 * Year is an immutable calendrical that represents a year.
 * This class does not store or represent a month, day, time or time zone.
 * Thus, for example, the value "2007" can be stored in a Year.
 * <p>
 * Year is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 */
public final class Year
        implements Calendrical, Comparable<Year>, Serializable {

    /**
     * Constant for the minimum year on the proleptic ISO calendar system.
     */
    public static final int MIN_YEAR = Integer.MIN_VALUE + 2;
    /**
     * Constant for the maximum year on the proleptic ISO calendar system,
     * which is the same as the maximum for year of era.
     */
    public static final int MAX_YEAR = Integer.MAX_VALUE;
    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = 2751581L;

    /**
     * The year being represented.
     */
    private final int year;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>Year</code>.
     * <p>
     * This method accepts a year value from the proleptic ISO calendar system.
     * <p>
     * The year 2AD/CE is represented by 2.<br />
     * The year 1AD/CE is represented by 1.<br />
     * The year 1BC/BCE is represented by 0.<br />
     * The year 2BC/BCE is represented by -1.<br />
     *
     * @param isoYear  the ISO proleptic year to represent, from MIN_YEAR to MAX_YEAR
     * @return the created Year, never null
     * @throws IllegalCalendarFieldValueException if the field is invalid
     */
    public static Year isoYear(int isoYear) {
        ISOChronology.INSTANCE.yearRule().checkValue(isoYear);
        return new Year(isoYear);
    }

    /**
     * Obtains an instance of <code>Year</code> using an era.
     * <p>
     * This method accepts a year and era to create a year object.
     *
     * @param era  the era to represent, either BC or AD, not null
     * @param yearOfEra  the year within the era to represent, from 1 to MAX_YEAR
     * @return the year object, never null
     * @throws IllegalCalendarFieldValueException if either field is invalid
     */
    public static Year year(Era era, int yearOfEra) {
        if (era == null) {
            throw new IllegalCalendarFieldValueException("era must not be null");
        }
        if (yearOfEra < 1) {
            throw new IllegalCalendarFieldValueException("year of era", yearOfEra, 1, MAX_YEAR);
        }
        if (era == Era.AD) {
            return Year.isoYear(yearOfEra);
        } else {
            return Year.isoYear((-yearOfEra) + 1);
        }
    }

    /**
     * Obtains an instance of <code>Year</code> from a set of calendricals.
     * <p>
     * This can be used to pass in any combination of calendricals that fully specify
     * a calendar year. For example, Century + YearOfCentury.
     *
     * @param calendricals  a set of calendricals that fully represent a calendar year
     * @return a Year object, never null
     */
    public static Year year(Calendrical... calendricals) {
        return new Year(0);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param year  the year to represent
     */
    private Year(int year) {
        this.year = year;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the year value.
     *
     * @return the year
     */
    public int getValue() {
        return year;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the calendrical state which provides internal access to this
     * instance.
     *
     * @return the calendar state for this instance, never null
     */
    @Override
    public CalendricalState getCalendricalState() {
        return null;  // TODO
    }

    /**
     * Checks if the specified calendar field is supported.
     * <p>
     * This method queries whether this <code>Year</code> can
     * be queried using the specified calendar field.
     *
     * @param field  the field to query, not null
     * @return true if the field is supported
     */
    public boolean isSupported(TimeFieldRule field) {
        return field.isSupported(Periods.YEARS, Periods.FOREVER);
    }

    /**
     * Gets the value of the specified calendar field.
     * <p>
     * This method queries the value of the specified calendar field.
     * If the calendar field is not supported then an exception is thrown.
     *
     * @param field  the field to query, not null
     * @return the value for the field
     * @throws UnsupportedCalendarFieldException if the field is not supported
     */
    public int get(TimeFieldRule field) {
        if (!isSupported(field)) {
            throw new UnsupportedCalendarFieldException(field, "year");
        }
        if (field == ISOChronology.INSTANCE.yearRule()) {
            return year;
        }
        return field.getValue(null);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this Year with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendrical  the calendrical values to update to, not null
     * @return a new updated Year, never null
     */
    public Year with(Calendrical calendrical) {
        return null;
    }

    /**
     * Returns a copy of this Year with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendricals  the calendrical values to update to, no nulls
     * @return a new updated Year, never null
     */
    public Year with(Calendrical... calendricals) {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this Year with the specified period added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param period  the period to add, not null
     * @return a new updated Year, never null
     */
    public Year plus(PeriodView period) {
        // TODO
        return null;
    }

    /**
     * Returns a copy of this Year with the specified periods added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param periods  the periods to add, no nulls
     * @return a new updated Year, never null
     */
    public Year plus(PeriodView... periods) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this Year with the specified number of years added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the years to add
     * @return a new updated Year, never null
     * @throws ArithmeticException if the result cannot be stored
     */
    public Year plusYears(int years) {
        int newYear = MathUtils.safeAdd(year, years);
        return isoYear(newYear);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this Year with the specified number of years subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the years to subtract
     * @return a new updated Year, never null
     * @throws ArithmeticException if the result cannot be stored
     */
    public Year minusYears(int years) {
        int newYear = MathUtils.safeSubtract(year, years);
        return isoYear(newYear);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this year to another year.
     *
     * @param other  the other year to compare to, not null
     * @return the comparator value, negative if less, postive if greater
     * @throws NullPointerException if <code>other</code> is null
     */
    public int compareTo(Year other) {
        return MathUtils.safeCompare(year, other.year);
    }

    /**
     * Is this year after the specified year.
     *
     * @param other  the other year to compare to, not null
     * @return true if this is after the specified year
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isAfter(Year other) {
        return year > other.year;
    }

    /**
     * Is this year before the specified year.
     *
     * @param other  the other year to compare to, not null
     * @return true if this point is before the specified year
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isBefore(Year other) {
        return year < other.year;
    }

    //-----------------------------------------------------------------------
    /**
     * Is this year equal to the specified year.
     *
     * @param other  the other year to compare to, null returns false
     * @return true if this point is equal to the specified year
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Year) {
            return year == ((Year) other).year;
        }
        return false;
    }

    /**
     * A hashcode for this year.
     *
     * @return a suitable hashcode
     */
    @Override
    public int hashCode() {
        return year;
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs the string form of the year.
     *
     * @return the string form of the year
     */
    @Override
    public String toString() {
        // TODO: prefix to 4 digits
        return Integer.toString(year);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the year is a leap year, according to the ISO proleptic
     * calendar system rules.
     * <p>
     * This method follows the current standard rules for leap years.
     * In general, a year is a leap year if it is divisible by four without
     * remainder. However, years divisible by 100, are not leap years, with
     * the exception of years divisible by 400 which are.
     * <p>
     * For example, 1904 is a leap year it is divisble by 4.
     * 1900 was not a leap year as it is divisble by 100, however 2000 was a
     * leap year as it is divisible by 400.
     * <p>
     * This calculation is proleptic - applying the same rules into prehistory.
     * This is historically inaccurate, but is correct for the ISO8601 standard.
     *
     * @return true if the year is leap, false otherwise
     */
    public boolean isLeap() {
        return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
    }

    /**
     * Returns the next year.
     *
     * @return the next year, never null
     * @throws IllegalCalendarFieldValueException if the maximum year is reached
     */
    public Year next() {
        if (year == MAX_YEAR) {
            throw new IllegalCalendarFieldValueException("Year is already at the maximum value");
        }
        return isoYear(year + 1);
    }

    /**
     * Returns the next leap year after the current year.
     * The definition of a leap year is specified in {@link #isLeap()}.
     *
     * @return the next leap year after this year
     * @throws IllegalCalendarFieldValueException if the maximum year is reached
     */
    public Year nextLeap() {
        Year temp = next();
        while (!temp.isLeap()) {
            temp = temp.next();
        }
        return temp;
    }

    /**
     * Returns the previous year.
     *
     * @return the previous year, never null
     * @throws IllegalCalendarFieldValueException if the maximum year is reached
     */
    public Year previous() {
        if (year == MIN_YEAR) {
            throw new IllegalCalendarFieldValueException("Year is already at the minimum value");
        }
        return isoYear(year - 1);
    }

    /**
     * Returns the previous leap year before the current year.
     * The definition of a leap year is specified in {@link #isLeap()}.
     *
     * @return the previous leap year after this year
     * @throws IllegalCalendarFieldValueException if the minimum year is reached
     */
    public Year previousLeap() {
        Year temp = previous();
        while (!temp.isLeap()) {
            temp = temp.previous();
        }
        return temp;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the ISO proleptic year, from MIN_YEAR to MAX_YEAR.
     * <p>
     * The year 2AD/CE is represented by 2.<br />
     * The year 1AD/CE is represented by 1.<br />
     * The year 1BC/BCE is represented by 0.<br />
     * The year 2BC/BCE is represented by -1.<br />
     *
     * @return the ISO proleptic year, from MIN_YEAR to MAX_YEAR
     */
    public int getISOYear() {
        return year;
    }

    /**
     * Returns a new <code>Year</code> instance with a different year.
     * <p>
     * The year 2AD/CE is represented by 2.<br />
     * The year 1AD/CE is represented by 1.<br />
     * The year 1BC/BCE is represented by 0.<br />
     * The year 2BC/BCE is represented by -1.<br />
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param isoYear  the year to represent, from MIN_YEAR to MAX_YEAR
     * @return a new updated Year, never null
     */
    public Year withISOYear(int isoYear) {
        ISOChronology.INSTANCE.yearRule().checkValue(isoYear);
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the year of era, from 1 to MAX_YEAR, which is used in combination
     * with {@link #getEra()}.
     * <p>
     * The year 2AD/CE is represented by 2.<br />
     * The year 1AD/CE is represented by 1.<br />
     * The year 1BC/BCE is represented by 1.<br />
     * The year 2BC/BCE is represented by 2.<br />
     *
     * @return the year of era, from 1 to MAX_YEAR.
     */
    public int getYearOfEra() {
        return (year > 0 ? year : -(year - 1));
    }

    /**
     * Gets the era, either AD or BC.
     *
     * @return the era, never null
     */
    public Era getEra() {
        return (year > 0 ? Era.AD : Era.BC);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the century of era, from 0 to MAX_YEAR / 100.
     * <p>
     * This method uses a simple definition of century, being the
     * year of era divided by 100.
     * <p>
     * The value 20 will be returned from 2000AD/CE to 2099AD/CE.<br/>
     * The value 19 will be returned from 1900AD/CE to 1999AD/CE.<br/>
     * The value 0 will be returned from 1AD/CE to 99AD/CE.<br/>
     * The value 0 will be returned from 99BC/BCE to 1BC/BCE.<br/>
     * The value 1 will be returned from 1000BC/BCE to 1999BC/BCE.<br/>
     *
     * @return the century of era, from 0 to MAX_YEAR / 100.
     */
    public int getCenturyOfEra() {
        return getYearOfEra() / 100;
    }

    /**
     * Gets the millenium of era, from 0 to MAX_YEAR / 1000.
     * <p>
     * This method uses a simple definition of millenium, being the
     * year of era divided by 100.
     * <p>
     * The value 2 will be returned from 2000AD/CE to 2999AD/CE.<br/>
     * The value 1 will be returned from 1000AD/CE to 1999AD/CE.<br/>
     * The value 0 will be returned from 1AD/CE to 999AD/CE.<br/>
     * The value 0 will be returned from 999BC/BCE to 1BC/BCE.<br/>
     * The value 1 will be returned from 1000BC/BCE to 1999BC/BCE.<br/>
     *
     * @return the millenium of era, from 0 to MAX_YEAR / 1000.
     */
    public int getMilleniumOfEra() {
        return getYearOfEra() / 1000;
    }

    /**
     * Gets the decade of century, from 0 to 9.
     * <p>
     * This method uses a simple definition of decade, being the
     * remainder of the year of era divided by 10.
     * <p>
     * The value 2 will be returned from 2020AD/CE to 2029AD/CE.<br/>
     * The value 1 will be returned from 2010AD/CE to 2019AD/CE.<br/>
     * The value 0 will be returned from 2000AD/CE to 2009AD/CE.<br/>
     * The value 9 will be returned from 1990AD/CE to 1999AD/CE.<br/>
     *
     * @return the decade of century, from 0 to 9.
     */
    public int getDecadeOfCentury() {
        return (getYearOfEra() % 100) / 10;
    }

}