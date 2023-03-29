# SLS-wellbeing-app

## Git Naming Conventions Used:

https://codingsight.com/git-branching-naming-convention-best-practices/

Issues: Description (Add Label) <br />
Branches: Prefix[-ID]-Description <br />
Commits: Description [BuzzWord] #ID

Prefixes: wip, feature, bug

## Build and Run
This project can be imported into Android Studio (version 4+) using the built-in VCS system in Studio.

## Structure and Files

### Adapter Directory
The adapters are used to take data and put it into recycler views.  They've been named according to their function

#### When logged in as user
* CourseAdapter used for courses
* MoodAdapter renders the list of moods in the mood history section
* ResourceAdapter displays the different resources and links
* TaskAdapter displays the list of tasks on when 

#### When logged in as mentor
* MentorTaskAdapter used for displaying the mentees tasks
* UserAdapter displays the list of users

#### When logged in as admin
* UserAdapter displays the list of users

### Database Directory
These are the files that are used for storing and retrieving data in the local SQLite database.  Currently only moods, tasks, and users are stored in the local DB.
* DatabaseHandler is the single file used to instantiate the database and initialise the tables
* MoodDatabaseHandler used to insert, retrieve, modify, delete user moods.  The `userId` property is a foreign key to reference the user who has created and stored their mood
* TasksDatabaseHandler used to insert, retrieve, modify, delete tasks assigned to user.  The `userId` property is a foreign key to reference the user that a mentor has assigned the task to
* UserDatabaseHandler used to insert, retrieve, modify, delete users from the system

### Model Directory
Here are the data models used for each type of data.  All models contain an `id` property so they can be moved into and out of their tables.

#### CourseModel
* title - course title
* description - describes the course

#### MoodModel
* userId - ID of the user who created the mood
* status - can be one of `happy`, `neutral`, `sad`
* date - the date the mood was created
* note - any note or journal-type of entry that the user might wish to add

#### ResourceModel
* title - title of the resource
* description - more information about the resource
* image - optional image to be displayed with the resource

#### TaskModel
* userId - ID of the user that the task is assigned to
* title - title of the task
* description - more information about the task
* status - maybe be `0` or `1` for uncompleted or completed respectively

#### UserModel
* email - user email address.  Used for login
* password - currently stored in plaintext.  This would need to be salted and hashed before actual use
* isMentor - boolean `true` or `false`.  The database stores this as `0` or `1` and the UserDatabaseHandler converts between the two.  This way the app can use the boolean property
* firstName - self explanatory
* lastName - self explanatory
* mentorId - ID of the mentor that has been assigned to the user.  Currently each user can only have one mentor

### Utils
This is mostly made up of helper classes to avoid duplicating code.

* DateUtils - Used to convert a Date to a database-compatible String and convert the text from the DB to a date object
* DummyData - Generates sample data that can be inserted into the DB to prefill data for this app.  Not needed for a production project, but useful for the initial prototype
* MoodChart - Use the chart library from [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) to generate the mood chart displayed on the user's mood tracker page and the user's profile when viewed by a mentor
* Popup - Easy-to-use functions to show simple dialogs
* SocialIntents - Handles showing or hiding Email, Phone, or SMS buttons in the Mentor and User card as seen on the Get Help page (user login) and the User Profile page (mentor login).  These buttons will also open the phone, SMS, or email app on the phone when clicked (does not work in emulator)
* SubActivity - Simple class to display and enable the back button on any activity view that implements it

### Other Files
* Activity files have Activity at the end of their name
* AddUserDialog - used by the Admin to create and edit user profiles as well as assign mentors
* PreferenceData - stores simple local settings that can be used throughout the app such as logged in user ID, type of user logged in and whether or not someone is logged in.

## res Directory
* `anim` contains the animation files
* `layout` and `layout-land` contain the layouts and landscape layouts for activities, adaptors, dialogs and more
* `menu` just has the logout button
* other directories and files should be according to standard Android app practices

## Tests
Tests are currently configured with GitHub actions to automatically get run on all pull requests.
* Utils directory tests some of the Utils classes
* The main directory has a sub-directory for testing the DB handler classes and the Activity classes
