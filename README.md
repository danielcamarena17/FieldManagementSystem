# Field Management System

The Field Management System (FMS), is an Android application that allows supervisors to keep track of their employee’s work hours. All data entry will be done onsite through a networked mobile device. The Field Management System is composed of three parts. The first part is an Android application that allows the supervisor to clock in employees via QR Code. The employee will carry a QR Code that the Android application will scan by using the camera on the mobile device. The second part will connect the Android application to a database hosted on Google’s Firebase. All employee data will be stored on the database and will only allow authenticated users to log into the Field Management System application. The last part will be connecting the Android Application to Google’s Sheets, where all employee hours will be stored. Google’s Sheets will allow easy access to all employee hours from any networked device. The objective is reducing the time it takes to log employee hours, eliminate the wait time before the company has access to the logged hours, and eliminating the need for a dedicated person to enter the logged hours. 

![alt text](doc/chart.jpg)

## Objectives

A) Create a space where the supervisor has all the resources available to collect employee hours quickly and efficiently 
B) Create a space where employee hours are immediately available to the company after data collection
C) Create a space where data collection is secured
D) Reduce time spent logging employee hours
E) Eliminate the need to travel to the company and submit the logged hours 
F) Eliminate the wait time before the company has access to the logged hours
G) Eliminate conventional means to record employee work hours such as pencil and paper
H) Eliminate the need for a data entry employee at the company to log hours into system

## Features

A) QR Code Scanner
B) QR Code Generator
C) QR Code can be sent via text, email, or any android app that can send photos
D) Realtime database
E) Password reset via email link
F) Employee timesheet is updated realtime
G) Employee timesheet can be downloaded as an excel file
H) The FMS application is secure because it requires user authentication via email link

## Demo Video
