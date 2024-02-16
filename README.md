# Project Details
Project Name: Sprint 2 - Server

Team members: fsosa and bho15

Github repo: https://github.com/cs0320-s24/server-fsosa-bho15

# Design Choices
We elected to create 5 different packages that each deal with different parts of the server. The server package contains the server class, and the handlers for the endpoints which interact with each other. One major design choice we made in these classes was to pass in a LoadCSVHandler object to the view and search handlers so that they would know if the csv was correctly loaded or not. 

Another one of the packages was an exceptions package, which included specific exceptions that we wanted to throw based on the type of error that was occurring. We also had a data source package, which managed the cache, and both the mock and real census data, as well as the proxy interface. Lastly, we have a csvparser package to deal with parsing and searching the csv's once they are loaded, and a census package to do things like deserialize the census data. 

# Errors/Bugs
N/A
# Tests
We had 3 test classes, one for integration testing with the census API, one for unit testing the csv endpoints, and one for testing the cache. 

The integration test class ensures our census API calls work correctly by making calls with different parameters and edge cases and ensuring we get the expected response. 

The unit test class makes sure the functionality for loading, viewing, and searching csvs works as intended. It also tests to make sure the mocking works as intended. 

The cache test class checks that the cache is operating on the census data properly. It has different tests that make sure data is being cached, unloaded, and that the program still works with no cache. 

# How to
mvn package to compile and run tests

./run to start up server on localhost:3241

To use the endpoints:

load - localhost:3241/load?filepath=[INPUT CSV FILE HERE]

view - localhost:3241/view

search - localhost:3241/search?attribute=[DESIRED ATTRIBUTE HERE]

optionally, to specify a columnIndex or columnName - 

localhost:3241/search?attribute=[DESIRED ATTRIBUTE HERE]&columnName=[COLUMN NAME HERE] or 

localhost:3241/search?attribute=[DESIRED ATTRIBUTE HERE]&columnIndex=[COLUMN INDEX HERE]

broadband - localhost:3241/broadband?state=[STATE NAME]&county=[COUNTY NAME]
