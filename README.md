The purpose of this application is to generate a list of show times for movies.
It does so by being given a list of movies being shown with running times, as well as hours of business.
With this information, it then generates PDF file showing time schedules for both the weekends and weekdays.

The movies file contains the name of the movie, its ratings, release date, and running times.
The hours file contains the theater's weekend and weekday hours of operation.
 
The schedule is generated according to the following requirements:
* Each movie should start at easy to read times (eg 10:00, 10:05, 10:10) and the start time of the movie is exactly at the posted start time. Note: The runtime of each movie does not include time for previews or cleanup.
* Each movie requires 15 minutes for previews before the start of the movie, and each movie requires 20 minutes after its end time to prepare the theatre for the next movie. 
* The cinema requires 15 minutes after opening before the first movie is shown. No movie should end after the cinema’s hours of operation, and the last showing should end as close as possible to the end of the cinema’s hours of operation. 
 
This application takes in the details of each movie and outputs a start and end time of each showing that abides by all of the above mentioned requirements. 