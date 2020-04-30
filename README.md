# CourseApp: Assignment 0

## Authors
* Michael Landver, 204749022
* Albert Iancu, 322073453

## Notes

### Implementation Summary
CourseTorrent depends on IBencoder and IDatabase interfaces. Implementations of those interfaces get injected into its constructor. 
We have a Bencoder class, responsible for all bencoding functions, and a Database class responsible for storing information persistently.

### Testing Summary
CourseTorrent testing:

We made a 'fake' of the IDatabase interface in order to test CourseTorrent without depending on a real database. And we used a mock IBencoder in order to configure IBencoder's method calls that CourseTorrent uses.

Bencoder testing:
This class has no dependencies so it was simple to test.

Database testing:
This class depends on SecureStorage and we used static mocking in order to configure the read and write methods.
### Difficulties

### Feedback
Too much time was spent on low level stuff like hash functions, decoding, and unicode formats and not enough on Software Design.