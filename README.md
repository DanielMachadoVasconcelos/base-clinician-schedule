# base-clinician-schedule

[![Java CI with Gradle](https://github.com/DanielMachadoVasconcelos/base-clinician-schedule/actions/workflows/gradle.yml/badge.svg)](https://github.com/DanielMachadoVasconcelos/base-clinician-schedule/actions/workflows/gradle.yml)

Project for testing a scheduler api where clinicians can set up their own shift in a configurable way.
This project will also enable them to manage their schedule by adding booking time slots and getting available times slots.

### Basic requirements:
* Create API to insert Appointments and Shifts for a given specific Clinician;
* Create API to find all Bookable Clinician Availability time slots;

-------------
### Definitions

The following definitions represents the object universe of this project:

#### Time Slot
```Time slot``` represents a period o time that contains a start and an end point in time. 
 
* It **must** have a Duration (```duration = endTime - startTime```) grater then zero;
* The start time **must** be before the end date time;
* It **must** contain information about Date, Time and TimeZone; 

#### Appointment
```Appointment``` represents a future meeting between a list of participants during a possible ```Time Slot```.

* It **should** contains a list of ```participants``` that will meet in a future time slot to have an encounter;
* It **must** have at least 2 ```participants```;
* It **must** have at least 1 ```Clinician``` and 1 ```Patient```;
* It **must** contains a possible start and an end time (```Time Slot```);
* It **could** have a list of ```locations```; 

#### Shift
```Shift``` represents the period of time (```Time Slot```) that a ```Clinician``` can take ```Appointments```

* It **must** contains a hard start and an end time (```Time Slot```);
* It **must** be linked to one ```Clinician```;

#### Schedule
```Schedule``` represents the union of Shifts and Appointments for a given ```Clinician``` 

* It **must** contains a list of ```Appointments```
* It **must** contains a list of ```Shift```

#### Clinician
```Clinician```  represents a health care professional that can act as a practitioner perform medical tasks.
* It **must** have an identifier.

#### Patient
```Patient```  represents an individual person that seeks health care.
* It **must** have an identifier.

#### Participant
```Participant``` represents an individual that will have an appointment 
* It **could** be a ```Clinician``` or ```Patient```; 

> **_NOTE:_**  Those definitions can be updated at any time according to the need of the Domain.
-------------


