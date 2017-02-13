Time and Money Code Library
==============================

This is a project to develop code for manipulating basic concepts in recurring domains such as time and money. The design principles followed here are explained in Part III of the book Domain-Driven Design. Read the credits [here](readme/credits.txt) and [release notes](readme/) here. For some of the conceptual workings, you can read [this explanation](http://timeandmoney.sourceforge.net/timealgebra.html) of time algebra and [this explanation](http://timeandmoney.sourceforge.net/intervalmath.html) of interval math.

In addition to the unit tests, the `example.*` packages found in the "[examples/src](examples/src)" folder provide runnable code doing things typical of many applications. (More of that in future releases.) Usage is also discussed briefly below. Here you can read answers to some frequently asked questions.

This software is provided under the very permissive MIT Licence (see LICENSE), so no one should be impeded from trying it.

Getting Started with Time Language
------------------------------------

The TimeLanguage is a set of classes and interfaces that embodied our basic model of time, allowing representation and manipulation of time values, and providing the language to express time related things.The Java library's date classes make several things hard and obscure that could be easy and clear.

One initial goal was to make object creation convenient and readable. This is not terribly important in application code, where dates usually come from the UI or a database and are manipulated in the program, but it is very important in writing the tests and making them clear. So as you read the tests, instead of seeing

```java
Calendar calendar = Calendar.getInstance();
calendar.setTimeZone(TimeZone.getTimeZone("Universal");
calendar.set(Calendar.DATE, 5);
calendar.set(Calendar.MONTH, 3 - 1);
calendar.set(Calendar.YEAR, 2004);
Date march5_2004 = calendar.getTime();
```

you will see

```java
TimePoint march5_2003 = TimePoint.atMidnightGMT(2004, 03, 5);
```

or perhaps

```java
TimePoint march5_2003 = CalendarDate.date(2004, 03, 5);
```

in cases where we really mean the date, and not a particular moment in time. The use of exact times to represent dates is typical of the limitations of using the existing Java library. (CalendarDates are discussed below.)

Two other concepts that occur in much of the date logic in business applications are duration and interval. A duration is an length of time. Curiously, there is no explicit way to say this using the Java library classes. Such durations are usually stored as integers. So, for example,

```java
class Invoice {

 Date dueDate;
 int gracePeriodInDays;

 boolean isDelinquent(Date currentDate) {
  Calendar calendar = Calendar.getInstance();
  calendar.setTime(dueDate);
  calendar.add(Calendar.DATE, gracePeriodInDays);
  delinquencyDate = calendar.getTime();
  return currentDate.after(delinquencyDate);
 }

}
```

The grace period variable can't be interpreted by itself -- only by reading its name, looking at code that manipulates it, and making educated guesses. Also, the Calendar's add method is awkward, and actually modifies the Calendar. In Time Language, it would be handled like this:

```java
class Invoice {

 TimePoint due;
 Duration gracePeriod;

 boolean isDelinquent(TimePoint now) {
  return now.isAfter(due.plus(gracePeriod));
 }

}
```

The expression dueDate.plus(gracePeriod) is as TimePoint, defined by the calculation. Neither object used in the expression is modified by the operation, so the expression can be evaluated any number of times with the same, predictable result. In fact, most operations in timeandmoney are side-effect-free functions, and most objects are immutable value objects.

Another time concept in the Time Language is interval, the period between two points in time. (Actually Interval is quite general, and can be used for other things, but we'll focus on time here.) TimeInterval is used to make boolean expressions like this:

```java
aTimeInterval.includes(aTimePoint)
```

It can be defined either by its two endpoints or by one endpoint and a duration. So, given these variables,

```java
TimePoint march5_0hr = TimePoint.atMidnightGMT(2003, 3, 5);
TimePoint march7_0hr = TimePoint.atMidnightGMT(2003, 3, 7);
Duration twoDays = Duration.days(2);
```
the following three expressions are all equivalent TimeIntervals (equals comparisons would be true):

```java
march5_0hr.until(march7_0hr)

twoDays.startingFrom(march5_0hr)

TimeInterval.over(march5_0hr, march7_0hr)
```

If our hypothetical invoicing application is required to decide if a particular charge for work should be included in the current invoice, it can check by evaluating this statement:

```java
invoice.billingInterval().includes(charge.getWorkTime());
```

where the Invoice has this behavior:

```java
class Invoice {
 ...

 Date billingStart;
 Date billingEnd;

 boolean includeCharge(charge) {
   Date chargeDate = 
       charge.getWorkTime()
   return chargeDate.after(billingStart) && chargeDate.before(billingEnd);
 }

}

class Invoice {
 ...

 TimeInterval billingPeriod;

 boolean includeCharge(charge) {
   return billingPeriod.includes(
      charge.getWorkTime());
 }

}
```

Not only is the Time Language version more concise, it is less error prone. A thorough unit test is advised for logic like that on the left.

These three concepts form a core for understanding the manipulation of actual time in Time Language, although there are other concepts with corresponding classes and other operations to flesh out the capabilities suggested by those concepts. These are best explored by reading the code and the tests found in the tests folder, especially the JUnit tests in the packages named `example`.

Calendar Dates
---------------

The other major distinction, which is treated partially in the Java library, but in a clumsy way, is the distinction between actual times and the marking of calendar dates. There are times when you just want to say March 5, 2004, and you don't mean "at midnight" or any other time. Surprisingly, the Java library lacks this. The TimePoint/TimeInterval logic above is mirrored with CalendarDate/CalendarInterval logic. So, for example:

```java
CalendarDate march5_2003 = CalendarDate.from(2003, 3, 5);
CalendarDate march7_2003 = CalendarDate.from(2003, 3, 7);
Duration twoDays = Duration.days(2);
```

the following three expressions are equivalent CalendarIntervals (`.equals()` would return `true`):

```java
march5_2003.until(march7_2003)

twoDays.startingFrom(march5_2003) //later release

CalendarInterval.over(march5_2003, march7_2003)
```

But there are important differences, which show up especially in the mapping between the two.

```java
march5_2003.until(march7_2003).asTimeInterval(aTimeZone)
```

requires a time zone to be specified. And a CalendarDate actually translates into a TimeInterval, not a time point -- it specifies a whole day. So the following two expressions are equivalent:

```java
march5_2003.asTimeInterval(TimeZone.getTimeZone("Universal"))

march5_0hr.until(march6_0hr)
```

Selective Abstraction - Careful Implementation
---------------------------------------------------

More abstraction and generalization is not always better. In the Java library, the separation of the Calendar class seems like a nice abstraction, but it was not implemented well. The result is that the great majority of applications that use conventional calendars have to deal with cumbersome constants because Calendar attempts to make no assumptions about the breakdown of time into years, months, days, and so forth, resulting in verbose, unreadable code.

few applications need the versatility to define their own calendar
most of those (eg fiscal calendars) which do are still made up of months, days, and years
when a calendar is truely different, the applications needs usually are much more specialized (e.g. astrological calculations) and the Java date functions will be inadequate anyway.
To make matters worse, the Calendar must be involved in almost every operation involving Dates. The Calendar makes common needs complicated and adds little versatility that is actually used. In contrast, the BusinessCalendar included in the Time Language is completely optional for applications that don't need it. TimePoints are fully functional without it. But the BusinessCalendar adds useful capabilities such as defining holidays. (As a side note, TimePoint uses the Java Calendar class internally, because it does have useful functionality, but the Calendar is encapsulated inside a completely different interface.)

As the Time Language develops (along with the other modules of the Time & Money Library) new features and abstractions will be added in response to the actual needs of the projects that use it. Refinements will be aimed at simplifying its use and making it suppler, easier to mold into the forms people need, and more fluid in its expressions.

Why This Is Needed
====================

On project after project, software developers have to reinvent the wheel, creating objects for simple recurring concepts such as "money" and "currency". Although most languages have a "date" or "time" object, these are rudimentary, and do not cover many needs, such as recurring sequences of time, durations of time, or intervals of time.

This project will develop an open source code library based on fundamental concepts of time and money and possibly other basic recurring domains. This will not just be a jumble of handy features. Care will be taken to distill model concepts and produce coherent designs that are simple to understand, use, and change to fit the needs of a variety of different application development projects.

Initial work will be in Java, but the conceptual models will probably not be language specific, so parallel implementations may be written in other popular languages. In addition, it will be important to develop basic adaptations for common object-relational mapping tools, xml representations, and other common integrations.

Our goal is to allow programmers to spend their time on solving the higher-level problems of their own projects because they have a trustworthy and flexible library of lower-level elements. We want to raise the level of abstraction for programming.

Code libraries of basic time and money functionality, similar to the one we plan, have been written before. In fact, they have been written many times. The trouble is, they have generally been part of proprietary development projects, and so they can never be used by others. Our team has worked on some of those projects; we have written some of those libraries.

There have been attempts to create such software for wider use. They have not succeeded for various reasons. Some libraries have been written as commercial software. They have been too expensive, and their licenses to constraining for the value they bring. This expense pushes them to create more comprehensive approaches, providing entire frameworks for application development instead of just solving a particular problem, but their frameworks turn out too limiting for most projects. And some of these past attempts have simply not been well-designed. The few attempts we have seen to develop free software were not deep enough. They dabbled in the right areas but did not solve the problems, as we, who have needed this software before and will again, intend to do.

Our team is made up of experts in object design. Our ambitions are modest. We will focus on a few narrow problems and solve them with care. The design is aimed at making common application issues straightforward and complex needs more practical, while the code is left open so that users can modify it for special needs, and, we hope, contribute their improvements to the community.

 
