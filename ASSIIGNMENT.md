**Objective**  
In this homework assignment, we want you to become familiar with the basics of using Java Swing to create a GUI interface. Unlike in future assignments, where you'll be extending Swing to do new things, here you'll only be assembling already-provided Swing components to create a user interface.

Future assignments will build on this first assignment, so it's important for you to get this one right and to understand what you have done.

PLEASE NOTE: You are NOT allowed to use any type of GUI builder tool for this assignment (anything that allows you to graphically lay out an interface and then automatically generates code for it). The reason is that this assignment is designed to get you up-to-speed on swing through writing some basic Swing code; if you're using a GUI builder, you're not getting that practice.

If you're unsure about the tools you're using, check with us first.

**Description**  
During the course of this semester, we will be creating an interactive calendar application similar to the Microsoft Outlook calendar or Apple's iCal. When completed, the application will allow you to view days and months, and add and edit appointments in a variety of ways.

In this homework, we'll create the basic "shell" of the application. The goal of this homework is to simply give you some practice writing basic Swing GUIs (using existing components instead of creating your own), and to make sure that your environment is set up correctly.

Be sure to pay attention to the instructions at the bottom on how to turn in the assignment, especially the part about building and submitting an executable JAR file.

Basic Interface Layout
Your application should create a new window (a JFrame component) when it starts. The components and behaviors that your application must provide in this window are:

A menu bar (Links to an external site.) with the following menus (Links to an external site.) (see this tutorial (Links to an external site.) for more details on how to set up menus):

**File:**  
Exit. Selecting this menu item should quit the application.
View: This menu should contain two radio-style buttons (see JRadioButtonMenuItem (Links to an external site.)) that lets the user select between two "view modes". These view modes should be mutually exclusive, meaning that when one is selected the other is de-selected.
Day View. Day view means that a single day will be displayed at a time in the application. This should be the default selection, and is what we'll implement for this homework.
Month View. Month view means that a grid showing all the days of the month should be displayed.  
NOTE that you do not have to implement any actual day or month views yet! We'll do these in later homeworks. For now, the View menu controls should properly toggle back and forth between the two modes, and update a label that shows the current date (described below).

Along the bottom of the frame should be a label (see JLabel (Links to an external site.)) that will serve as a status bar, and be used to display various messages. Any time a button or menu item is selected, the status bar should display the name of the control that was selected.

The main body of your window will be divided into two parts. On the left should be a side panel with controls for moving through the calendar, and for creating events. These controls should include:

A Today button that will change the view to the current day (when in day view mode) or month (when in month view mode).
Buttons for Next and Previous. These buttons will move forward and back through the days or months displayed in the calendar.
A New Appointment button. This will pop up a dialog box that lets the user enter details about a new calendar appointment.
The main portion of the window should be your content area. In the next homework assignment we'll make this area do more, but for now it should just display a simple text string (see JLabel) that indicates the day or month being displayed currently. In day view mode, the label should display something like "Day View: Tuesday, August 24, 2021" and in month view mode should display just "Month View: August 2021" (with no day or date). This label should properly update whenever you switch between the two view modes, and when you move through the days or months using the next/previous buttons.

Again, we'll be replacing this label with proper day and month views in later homeworks, so for now this is just to give feedback and ensure that the logic of your program is working correctly.

In order to make this work, you'll need to write Listener methods for your buttons that update the state of the label to show the date in the correct format. Likewise, in order to have the status bar display a message when something was clicked, you'll need listeners on all your controls that update the text in the status bar.

When the user clicks the New Appointment button, the application should display a dialog box that allows the user to enter in details of a new calendar event. The dialog box is essentially its own little window that appears when the button is clicked. It should have the following controls:

A text field (see JTextField) that allows the user to enter in a name for the new appointment, with "New Event" filled in as the default.
A text field that displays the currently displayed date as the default, but which the user can type over.
A start and end time for the event. These can also just be editable text fields, or you can use something fancier if you want.
A collection of at least four buttons with tag names, such as "Vacation," "Family," "School," "Work," and so on; you're welcome to come up with your own tag names. The behavior of this group of buttons is that the user should be able to flag calendar events with zero or more of these tags; so, for instance, an event might be tagged with both "Vacation" and "Family."  To do this you'll probably want to use either toggle buttons (Links to an external site.) or check boxes (Links to an external site.) so that the buttons stay in their selected or unselected state after the user clicks on them.
Finally, at the bottom of the dialog box, a Cancel button (which dismisses the dialog without saving any changes), and an OK button (which creates the new calendar event).
This dialog box will be the way users will enter in new calendar appointments. For this current homework, we won't have a way to actually display these appointments though... that'll come in Homeworks 2 and 3. So for now, here's the behavior you should implement:

If the user clicks the Cancel button, then the status bar should just display something like "Create event dialog box cancelled."

If the user clicks the Ok button, then the status bar should display something like "New event created: Title, Date, Start Time - End Time" so that we can see that it's working correctly.

Throughout this assignment, and for both the main window and the dialog box, be sure to pay attention to your component hierarchy and how you're using layout managers: Your application needs to behave reasonably when resized. "Reasonably" means that the window should be resizable, but that buttons shouldn't get bigger, control panels cannot be shrunk in a way that crops their contents, and so forth. Likewise, enlarging the window should make the content size larger but not expand control panels unnecessarily. (Look at the way that most "standard" desktop apps behave when resizing, and ask if you have any questions.)  For the main window, my strong recommendation is to use a BorderLayout, with the panel containing the controls in the West region, the panel containing your center content area in the Center region, and your status label in the South region. You will likely need sub-panels (each with their own layout managers) for the left control panel and for the dialog box.

Dealing with Calendars
Obviously, the focus in this class is on the graphical interface and the interactive aspects of our application.  But, we do need to know a little bit about how to work with days and dates in order to build the app.

I recommend using the java.time.LocalDate class to work with dates. Using this class is pretty simple: each instance of LocalDate represents a date (think of it as Month, Day, Year, but without any associated time of day). Each instance of this class is also immutable, which means that you can't change it once it's been created.

To get a new LocalDate object that represents the current day, you'd do:

LocalDate today = LocalDate.now();
If you want to know what the previous or following date is, you could do:

LocalDate tomorrow = now.plusDays(1);

LocalDate yesterday = now.minusDays(1);
(The month and year will automatically be incremented or decremented if necessary, based on the date.)

Likewise, you can also add or subtract months, weeks, or years by calling plusMonths(), minusMonths(), and so forth.

You can convert a LocalDate into a string by either printing it (which gives you a default format), or you can have more control over the string representation by using the format() method, which takes a DateTimeFormatter as a parameter.

While for this assignment, we're just treating dates and times as simple strings, my strong recommendation is that you get used to using formats that can be easily parsed into a LocalDate object, as you'll need this going forward. Take a look at java.time.DateTimeFormatter to better understand this. For example, this code can convert strings of formats like "08/24/2021" into a LocalDate object:

// Create a DateTimeFormatter with the date pattern we want to use

DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyy");

String date = "08/24/2021";

// Create a local date that parses out a string according to the supplied format.

LocalDate localDate = LocalDate.parse(date, formatter);
These tips are probably sufficient to get you started, but if you're curious, there's much more to the java.time package, including classes that represent times, durations, and much much more.

Interactivity
This section summarizes the interactivity that your program should have for this first assignment. The things your application needs to do at this point are:

Control feedback. Clicking on any of the buttons or menu items in the application should display a message in the status bar at the bottom of the window (e.g., "Day view selected").
Today button should update the text of the label in the content area to reflect the current date or month, depending on the view mode you're in.
Next/Previous should update the text of the label in the content area, to reflect the next or previous date or month, depending on the view mode you're in.
Day/Month View should update the text of the label in the content area, switching between the day-specific format ("Day view: Tuesday, August 24, 2021") and the month-specific format ("Month view: August 2021").
New Appointment should pop up the dialog box, which can then be dismissed either via Cancel or Ok. The required text fields in the dialog box should be editable, although you don't need to do anything with this data yet, other than display it in the status bar when the dialog is dismissed.
Closing the application. The application can be terminated in two ways: selecting the Exit menu item, or clicking on the "close widget" on the JFrame (e.g., the red traffic light dot on the title bar on macOS). Look at JFrame.EXIT_ON_CLOSE (shown in our example code from lecture) for the latter.
Resizing. The application should behave "reasonably" when it's resized. Note that it's hard to describe every aspect of good resize behavior, but in general your application should behave like normal, commercial applications: the tool palette should be just wide enough to contain the tool buttons in it; the tool buttons themselves shouldn't get bigger or smaller as the window is resized. The content area, on the other hand, should grow and shrink both vertical and horizontally as the window is resized. The status bar label should only grow and shrink horizontally, not vertically. Again, you'll find BorderLayout helpful here.     
Extra Credit
In this and future assignments we'll suggest some ways you can earn extra credit on the project.

Some possibilities for this assignment are:

Replace the simple text fields in your dialog box with more sophisticated date and time pickers. Probably +2 or more depending on complexity.
Add nice-looking icons to the buttons in the control palette area. +2 (But please be sure to access these correctly, so that your program works when we try to run it!  Your icon images must be bundled into your JAR file so that it is "self-contained," and you'll need to access them in a particular way. See here (Links to an external site.) for details on how to use icon images from your JAR file.)
Other ideas are up to you. Just let us know what you did in your README file and we'll try to get you some extra points!

Deliverable
See here for instructions on how to submit your homework. These instructions will be the same for each assignment.

