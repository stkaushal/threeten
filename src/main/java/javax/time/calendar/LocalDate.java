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

import javax.time.calendar.field.DayOfMonth;
import javax.time.calendar.field.DayOfWeek;
import javax.time.calendar.field.DayOfYear;
import javax.time.calendar.field.MonthOfYear;
import javax.time.period.PeriodUnit;
import javax.time.period.PeriodView;
import javax.time.period.Periods;

/**
 * A calendrical representation of a date without a time zone, such as 2007-12-03.
 * <p>
 * LocalDate is an immutable calendrical that represents a date, often viewed
 * as year-month-day. This object can also access other date fields such as
 * day of year, day of week and week of year.
 * <p>
 * This class does not store or represent a time or time zone.
 * Thus, for example, the value "2nd October 2007" can be stored in a LocalDate.
 * <p>
 * LocalDate is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 */
public final class LocalDate
        implements ReadableDate, Calendrical, Comparable<LocalDate>, Serializable {

    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = 798274969L;

    /**
     * The year, not null.
     */
    private final Year year;
    /**
     * The month of year, not null.
     */
    private final MonthOfYear month;
    /**
     * The day of month, not null.
     */
    private final DayOfMonth day;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>LocalDate</code> from a year, month and day.
     *
     * @param year  the year to represent, not null
     * @param monthOfYear  the month of year to represent, not null
     * @param dayOfMonth  the day of month to represent, not null
     * @return a LocalDate object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static LocalDate date(Year year, MonthOfYear monthOfYear, DayOfMonth dayOfMonth) {
        ISOChronology.INSTANCE.checkValidDate(year, monthOfYear, dayOfMonth);
        return new LocalDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * Obtains an instance of <code>LocalDate</code> from a year, month and day.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param monthOfYear  the month of year to represent, not null
     * @param dayOfMonth  the day of month to represent, from 1 to 31
     * @return a LocalDate object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static LocalDate date(int year, MonthOfYear monthOfYear, int dayOfMonth) {
        return date(Year.isoYear(year), monthOfYear, DayOfMonth.dayOfMonth(dayOfMonth));
    }

    /**
     * Obtains an instance of <code>LocalDate</code> from a year, month and day.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth  the day of month to represent, from 1 to 31
     * @return a LocalDate object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static LocalDate date(int year, int monthOfYear, int dayOfMonth) {
        return date(Year.isoYear(year), MonthOfYear.monthOfYear(monthOfYear), DayOfMonth.dayOfMonth(dayOfMonth));
    }

    /**
     * Obtains an instance of <code>LocalDate</code> from a set of calendricals.
     * <p>
     * This can be used to pass in any combination of calendricals that fully specify
     * a date. For example, Year + MonthOfYear + DayOfMonth, or Year + DayOfYear.
     *
     * @param calendricals  a set of calendricals that fully represent a calendar day
     * @return a LocalDate object, never null
     * @throws IllegalCalendarFieldValueException if any calendrical is invalid
     */
    public static LocalDate date(Calendrical... calendricals) {
        if (calendricals.length == 0) {
            throw new IllegalCalendarFieldValueException("No calendricals specified");
        }
        if (calendricals[0].getCalendricalState().getPeriodRange() != Periods.FOREVER) {
            throw new IllegalCalendarFieldValueException("First calendrical must have a range of forever");
        }
        if (calendricals[calendricals.length - 1].getCalendricalState().getPeriodUnit() != Periods.DAYS) {
            throw new IllegalCalendarFieldValueException("Last calendrical must have a unit of days");
        }
        PeriodUnit last = calendricals[0].getCalendricalState().getPeriodUnit();
        for (int i = 1; i < calendricals.length; i++) {
            if (calendricals[i].getCalendricalState().getPeriodRange() != last) {
                throw new IllegalCalendarFieldValueException("Calendricals do not form a continuous set: " +
                        last + " != " + calendricals[i].getCalendricalState().getPeriodRange());
            }
            last = calendricals[i].getCalendricalState().getPeriodUnit();
        }
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor, previously validated.
     *
     * @param year  the year to represent, not null
     * @param monthOfYear  the month of year to represent, not null
     * @param dayOfMonth  the day of month to represent, valid for year-month, not null
     */
    private LocalDate(Year year, MonthOfYear monthOfYear, DayOfMonth dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the calendrical state which provides internal access to
     * this date.
     *
     * @return the calendar state for this instance, never null
     */
    @Override
    public CalendricalState getCalendricalState() {
        return null;  // TODO
    }

    /**
     * Gets the chronology that describes the calendar system rules for
     * this date.
     *
     * @return the ISO chronology, never null
     */
    public ISOChronology getChronology() {
        return ISOChronology.INSTANCE;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified calendar field is supported.
     * <p>
     * This method queries whether this <code>LocalDate</code> can
     * be queried using the specified calendar field.
     *
     * @param field  the field to query, not null
     * @return true if the field is supported
     */
    public boolean isSupported(TimeFieldRule field) {
        return field.isSupported(Periods.DAYS, Periods.FOREVER);
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
            throw new UnsupportedCalendarFieldException(field, "date");
        }
        if (field == ISOChronology.INSTANCE.yearRule()) {
            return year.getValue();
        }
        if (field == ISOChronology.INSTANCE.monthOfYearRule()) {
            return month.getValue();
        }
        if (field == ISOChronology.INSTANCE.dayOfMonthRule()) {
            return day.getValue();
        }
        return field.getValue(getCalendricalState());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of <code>YearMonth</code> initialised to the
     * year and month of this date.
     *
     * @return the year-month object, never null
     */
    public YearMonth yearMonth() {
        return YearMonth.yearMonth(year, month);
    }

    /**
     * Gets an instance of <code>MonthDay</code> initialised to the
     * month and day of month of this date.
     *
     * @return the month-day object, never null
     */
    public MonthDay monthDay() {
        return MonthDay.monthDay(month, day);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the year field.
     * <p>
     * This method provides access to an object representing the year field.
     * This can be used to access the {@link Year#getValue() int value}.
     *
     * @return the year, never null
     */
    public Year getYear() {
        return year;
    }

    /**
     * Gets the month of year field.
     * <p>
     * This method provides access to an object representing the month field.
     * This can be used to access the {@link MonthOfYear#getValue() int value}.
     *
     * @return the month of year, never null
     */
    public MonthOfYear getMonthOfYear() {
        return month;
    }

    /**
     * Gets the day of month field.
     * <p>
     * This method provides access to an object representing the day of month field.
     * This can be used to access the {@link DayOfMonth#getValue() int value}.
     *
     * @return the day of month, never null
     */
    public DayOfMonth getDayOfMonth() {
        return day;
    }

    /**
     * Gets the day of year field.
     * <p>
     * This method provides access to an object representing the day of year field.
     * This can be used to access the {@link DayOfYear#getValue() int value}.
     *
     * @return the day of year, never null
     */
    public DayOfYear getDayOfYear() {
        return DayOfYear.dayOfYear(ISOChronology.INSTANCE.getDayOfYear(year, month, day));
    }

    /**
     * Gets the day of week field.
     * <p>
     * This method provides access to an object representing the day of week field.
     * This can be used to access the {@link DayOfWeek#getValue() int value}.
     *
     * @return the day of week, never null
     */
    public DayOfWeek getDayOfWeek() {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendrical  the calendrical values to update to, not null
     * @return a new updated LocalDate, never null
     */
    public LocalDate with(Calendrical calendrical) {
        if (calendrical instanceof Year) {
            return withYear(((Year) calendrical).getISOYear());
        }
        if (calendrical instanceof MonthOfYear) {
            return withMonthOfYear(((MonthOfYear) calendrical).getValue());
        }
        if (calendrical instanceof DayOfMonth) {
            return withDayOfMonth(((DayOfMonth) calendrical).getValue());
        }
        if (calendrical instanceof ReadableDate) {
            return ((ReadableDate) calendrical).toLocalDate();
        }
        // TODO
        return null;
    }

    /**
     * Returns a copy of this LocalDate with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendricals  the calendrical values to update to, no nulls
     * @return a new updated LocalDate, never null
     */
    public LocalDate with(Calendrical... calendricals) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @return a new updated LocalDate, never null
     */
    public LocalDate withYear(int year) {
        if (this.year.getValue() == year) {
            return this;
        }
        return CalendricalResolvers.previousValid().resolveDate(
                Year.isoYear(year), month, day);
    }

    /**
     * Returns a copy of this LocalDate with the month of year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @return a new updated LocalDate, never null
     */
    public LocalDate withMonthOfYear(int monthOfYear) {
        if (this.month.getValue() == monthOfYear) {
            return this;
        }
        return CalendricalResolvers.previousValid().resolveDate(
                year, MonthOfYear.monthOfYear(monthOfYear), day);
    }

    /**
     * Returns a copy of this LocalDate with the day of month value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfMonth  the day of month to represent, from 1 to 28-31
     * @return a new updated LocalDate, never null
     */
    public LocalDate withDayOfMonth(int dayOfMonth) {
        if (this.day.getValue() == dayOfMonth) {
            return this;
        }
        return date(year, month, DayOfMonth.dayOfMonth(dayOfMonth));
    }

    /**
     * Returns a copy of this LocalDate with the date set to the last day of month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return a new updated LocalDate, never null
     */
    public LocalDate withLastDayOfMonth() {
        return withDayOfMonth(month.lengthInDays(year));
    }

    /**
     * Returns a copy of this LocalDate with the date set to the last day of year.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return a new updated LocalDate, never null
     */
    public LocalDate withLastDayOfYear() {
        return new LocalDate(year, MonthOfYear.DECEMBER, DayOfMonth.dayOfMonth(31));
    }

    /**
     * Returns a copy of this LocalDate with the day of year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfYear  the day of year to represent, from 1 to 366
     * @return a new updated LocalDate, never null
     */
    public LocalDate withDayOfYear(int dayOfYear) {
        return null;
    }

    /**
     * Returns a copy of this LocalDate with the day of week value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfWeek  the day of week to represent, from 1 (Monday) to 7 (Sunday)
     * @return a new updated LocalDate, never null
     */
    public LocalDate withDayOfWeek(int dayOfWeek) {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the specified period added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param period  the period to add, not null
     * @return a new updated LocalDate, never null
     */
    public LocalDate plus(PeriodView period) {
        // TODO
        return null;
    }

    /**
     * Returns a copy of this LocalDate with the specified periods added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param periods  the periods to add, no nulls
     * @return a new updated LocalDate, never null
     */
    public LocalDate plus(PeriodView... periods) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the specified period in years added.
     * <p>
     * This method add the specified amount to the years field in three steps:
     * <ol>
     * <li>Add the input years to the year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day of month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2008-02-29 (leap year) plus one year would result in the
     * invalid date 2009-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2009-02-28, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the years to add, may be negative
     * @return a new updated LocalDate, never null
     * @throws ArithmeticException if the calculation overflows
     * @throws IllegalCalendarFieldValueException if the result contains an invalid field
     */
    public LocalDate plusYears(int years) {
        if (years == 0) {
            return this;
        }
        Year newYear = year.plusYears(years);
        return CalendricalResolvers.previousValid().resolveDate(newYear, month, day);
    }

    /**
     * Returns a copy of this LocalDate with the specified period in months added.
     * <p>
     * This method add the specified amount to the months field in three steps:
     * <ol>
     * <li>Add the input months to the month of year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day of month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2007-03-31 plus one month would result in the invalid date
     * 2007-04-31. Instead of returning an invalid result, the last valid day
     * of the month, 2007-04-30, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the months to add, may be negative
     * @return a new updated LocalDate, never null
     * @throws ArithmeticException if the calculation overflows
     * @throws IllegalCalendarFieldValueException if the result contains an invalid field
     */
    public LocalDate plusMonths(int months) {
        if (months == 0) {
            return this;
        }
        long newMonth0 = month.getValue() - 1;
        newMonth0 = newMonth0 + months;
        int years = (int) (newMonth0 / 12);
        newMonth0 = newMonth0 % 12;
        if (newMonth0 < 0) {
            newMonth0 += 12;
            years--;
        }
        Year newYear = year.plusYears(years);
        MonthOfYear newMonth = MonthOfYear.monthOfYear((int) ++newMonth0);
        return CalendricalResolvers.previousValid().resolveDate(newYear, newMonth, day);
    }

    /**
     * Returns a copy of this LocalDate with the specified period in weeks added.
     * <p>
     * This method add the specified amount in weeks to the days field incrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one week would result in the 2009-01-07.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the weeks to add, may be negative
     * @return a new updated LocalDate, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public LocalDate plusWeeks(int weeks) {
        return plusDays(7L * weeks);
    }

    /**
     * Returns a copy of this LocalDate with the specified period in days added.
     * <p>
     * This method add the specified amount to the days field incrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one day would result in the 2009-01-01.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days  the days to add, may be negative
     * @return a new updated LocalDate, never null
     */
    public LocalDate plusDays(int days) {
        return plusDays((long) days);
    }

    /**
     * Returns a copy of this LocalDate with the specified number of days added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days  the days to add
     * @return a new updated LocalDate, never null
     */
    private LocalDate plusDays(long days) {
        if (days == 0) {
            return this;
        }
        int monthLen = month.lengthInDays(year);
        long possDOM = day.getValue() + days;
        if (possDOM >= 1) {
            if (possDOM <= monthLen) {
                // same month
                return new LocalDate(year, month, DayOfMonth.dayOfMonth((int) possDOM));
            } else if (possDOM <= monthLen + 28) {
                // next month (28 guarantees only one month later)
                possDOM -= monthLen;
                if (month == MonthOfYear.DECEMBER) {
                    return new LocalDate(year.next(), MonthOfYear.JANUARY, DayOfMonth.dayOfMonth((int) possDOM));
                } else {
                    return new LocalDate(year, month.next(), DayOfMonth.dayOfMonth((int) possDOM));
                }
            }
        }
        long epochDays = 0L;
        epochDays += days;
        return null;  // TODO
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this date to a <code>LocalDate</code>, trivially
     * returning <code>this</code>.
     *
     * @return <code>this</code>, never null
     */
    public LocalDate toLocalDate() {
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this date to another date.
     *
     * @param other  the other date to compare to, not null
     * @return the comparator value, negative if less, postive if greater
     * @throws NullPointerException if <code>other</code> is null
     */
    public int compareTo(LocalDate other) {
        int cmp = year.compareTo(other.year);
        if (cmp == 0) {
            cmp = month.compareTo(other.month);
            if (cmp == 0) {
                cmp = day.compareTo(other.day);
            }
        }
        return cmp;
    }

    /**
     * Is this date after the specified date.
     *
     * @param other  the other date to compare to, not null
     * @return true if this is after the specified date
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isAfter(LocalDate other) {
        return compareTo(other) > 0;
    }

    /**
     * Is this date before the specified date.
     *
     * @param other  the other date to compare to, not null
     * @return true if this point is before the specified date
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isBefore(LocalDate other) {
        return compareTo(other) < 0;
    }

    //-----------------------------------------------------------------------
    /**
     * Is this date equal to the specified date.
     *
     * @param other  the other date to compare to, null returns false
     * @return true if this point is equal to the specified date
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LocalDate) {
            LocalDate otherDate = (LocalDate) other;
            return (year.equals(otherDate.year) &&
                    month == otherDate.month &&
                    day.equals(otherDate.day));
        }
        return false;
    }

    /**
     * A hashcode for this date.
     *
     * @return a suitable hashcode
     */
    @Override
    public int hashCode() {
        int yearValue = year.getValue();
        int monthValue = month.getValue();
        int dayValue = day.getValue();
        return (yearValue & 0xFFFFF800) ^ ((yearValue << 11) + (monthValue << 6) + (dayValue));
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs the string form of the date.
     *
     * @return the string form of the date
     */
    @Override
    public String toString() {
        int yearValue = year.getValue();
        int monthValue = month.getValue();
        int dayValue = day.getValue();
        int absYear = Math.abs(yearValue);
        StringBuilder buf = new StringBuilder(12);
        if (absYear < 1000) {
            if (yearValue < 0) {
                buf.append(yearValue - 10000).deleteCharAt(1);
            } else {
                buf.append(yearValue + 10000).deleteCharAt(0);
            }
        } else {
            buf.append(year);
        }
        return buf.append(monthValue < 10 ? "-0" : "-")
            .append(monthValue)
            .append(dayValue < 10 ? "-0" : "-")
            .append(dayValue)
            .toString();
    }

}