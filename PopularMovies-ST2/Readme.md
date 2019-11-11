### PopularMovies(Stage2)

What is it?
============

My implementation of the exam. 
The application access to the `[The movie datase](https://www.themoviedb.org/)`,
then retrieve its data from api they provided.

Library
------------

I use a lot of up to date library in this application 
with a focus on android jetpack.

+ Constraint layout
+ CardView
+ Data binding
+ Recyclerview paging library
+ Retrofit 2
+ Gradle
+ Room Persistence Library
+ Rxjava 2(for call Retrofit and Room)

Note
------------

+ To compile this application, You need to put themoviedb api key to local.properties file.
    + tmdb.apikey=`your tmdb api key here`
+ The application constructed with 2 Activities.
+ In this application, you can open trailer by youtube application or chrome.
    + For recent changes, In default every url related with youtube are force to open with youtube application.
    + So, I append a check box option `Force Chorome to open trailers` in Movie Detail Page.
        + When the option is checked, every link will be opened with chrome browser.
+ When I open the movie detail page, The program receives first 20 Reviews of the movie.
    + According to the api documents, each movies can contain 20000 reviews.
    + If we need to implement as per specification, we better to make another activity or fragment.
 
