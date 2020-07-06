# Edozo Code Test
Welcome to the Edozo Java Code test.

Our main project is Edozo Mapping, which enables users to create maps with annotations. 
You're to be tasked with building a simple service that allows users to search for maps.
 
## The test

We would like to see a simple web server that can:
- get all maps
- get a map by id
- search by address
- submit/save a map (this does not need to be persisted between application runs - in memory is fine)

Please return this data as `JSON`.

In the resource folder, you'll find have csv file that contains the initial data regarding a list of maps.  
A map consists of:
- id - unique integer for a map
- userId - an integer 
- address - a String
- bottomLeftX, bottomLeftY, topRightY, topRightY - the bounding box, each of which are doubles
- createdAt - a date as to when it was created
- updatedAt - a date as to when the map was last updated
- downloadUrl - a  url as to where the map can be downloaded

Please import and use libraries as you would normally for production use.

## What we're looking for / what to show off
We're not a big fan of tests that try to trip or confuse. So here's what at Edozo we look for:
- Clean structure
    - Well named
    - Easy to swap / change classes (think interfaces)
    - Obvious and consistent structure to packages, classes, and methods
- Knowledge of the language
    - Defaulted to Java 8 - so streams, optionals, etc. 
    - Use of later versions of Java (though stick to the GA releases please!)
- Knowledge of the Framework
    - Leveraging Springboot where possible
- Understanding web standards
    - Structure of endpoints
    - Use of Verbs 
- Observability
    - Logging, tracing, metrics
- Error handling
    - what happens when things go wrong
    - validation
- Testing
    - Unit testing the individual parts
    - Integration testing it working as a whole
    - Understanding what needs to be tested

There's some initial code to start with, but it can be removed / changed as desired.

**Final note**: Please focus on showing us the above, and less on being "feature complete". We give a guideline of roughly 4 hours to complete this challenge.

## Question for the next stage
For the next stage of the interview process, we'll be asking questions around how this application could be extended. Have a think about:
- What would happen if you needed to support a million records?
- What else could be searched for, and how could searching be implemented?
- What about if we had a consumer of this application that wanted updates in real time?
- What considerations would you need to make to have multiple instances of this service?
- How would you deploy this application?
- How would you triage this application?

Please don't include any answers to these questions in your submission. This is purely to think about. 

If you have any issues, or questions, please contact us!

Good luck!
The Edozo team.
