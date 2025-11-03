# What is Findr?

Findr is a social media app that serves posts to users with an algorithm based on location.
It was created as a playground to build/practice my knowledge of the Spring Framework,
but quickly evolved into a much more complex project that is now spread across two separate git repositories.
This repo is the backend API.

The front-end is linked here.
https://github.com/Benton60/FindrApp

## How Findr began
Findr began as the product of boredom during my freshman year of college. I was looking for side projects that would stretch my talents
and decided I wanted a project that stretched across a full tech stack. A social-media platform was the first thing that came to mind.
The project was never meant to become a fully fledged product, instead it was meant to provide a means to experiment with new technologies
in a production-scale environment. Many of my previous projects, i.e. both chess engines and the notes app, only required one development suite.

Chess Engines - Entirely backend development 
Notes App - Entirely Front-end Android development

I wanted a project that utilized everything at once, from the database to the UI. I settled one the Spring Framework as the backend due to my
previous experience with java and more specifically the JDBC. I chose an android client as the front end for the same reason. The project didn't begin
as a way to learn new technologies but more to see how the technologies I already used work together to deliver a full-scale product.
As the development process continued, however, I began to find new ideas and technologies that I wanted to implement, and Findr has continued to grow since then.


## API Specifics
FindrAPI is the git repository for the backend. I have it setup to connect to a local MySQL database. You can find the specifics in the application.properites file
The database calls are all controlled by the JDBC driver. The rest of the codebase follows standard Spring development architecture. 

### Points of Interest
1) The API uses spring's built-in security service
2) So far the database has tables for users, posts, and follow-relationships
3) The file handling and storage system was one of the best parts of the whole backend and works externally from the database
4) There is custom point serialization built-in to help reduce the complexity when storing locations in a database as they are stored in pure binary but are handled as JSON objects by the API

P.S. the README for the backend if fairly short because I did my best to follow industry standards so it follows the same structure as most API codebases
If you are curious as to any specific implementations the comments in individual files go into much greater detail as to how data is stored and transferred.