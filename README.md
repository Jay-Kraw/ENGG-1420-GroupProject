TO RUN THE PROGRAM
Create a test project file with an SRC folder
Add the packages "model", "service", "ui" into the SRC folder, as well as the nesiccary bookings.csv,users.csv and events.csv files
Run the MainApp.java in the UI package file to run and see the full UI.

HOW TO USE THE CAMPUS BOOKING SYSTEM
There are 4 button pannles on the left which each have a unique function

"USER MANAGEMENT"
The User management imports all of the users from users.csv file and displays them, it also allows you to create a new user by typing in the provided fields and then clicking export file to save them
- NOTE: New users will not be shown throughout the program in the other tabs, to be able to book events with new users you will need to close and rerun main app

"EVENT MANAGEMENT"
The Event management tab imports all of the events from events.csv file and displays them, it also allows you to create a new event by typing in the provided fields and then clicking export file to save them. 
- NOTE: New Events will not be shown throughout the program in the other tabs, to be able to book new events with users you will need to close and rerun main app

"BOOKING MANAGEMENT"
will allow users to book events by selecting events and user and clicking book events. it will only allow users to book a certain number of max events.
if the booking is filled the user will be placed on a waitlist untill a different user will cancel their booking.
-NOTE: to save new bookings ensure you click export file and rerun the program in order for those changes to be saved for the next instances.

"WAITLIST MANAGEMENT"
The waitlist management will display all information from the bookings.csv file. To search for an events and check its bookings open the dropdown menu and begin typing. New bookings from the "BOOKING MANAGEMENT" Tab will require you to hit refresh at the top left in order to see the new bookings and waitlist updates/changes

"SEARCH FEATURES"
in the "BOOKING MANAGEMENT" and "WAITLIST MANAGEMENT" tab click on the drop down menu and begin typing to search for any user or event by there name. 


TODO
- fix so we don't have to create a new SRC file and copy and paste project to make it work
- Adjust GUI such that added users do not require an exit from the application to be updated
