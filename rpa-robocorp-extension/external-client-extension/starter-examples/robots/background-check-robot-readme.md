# Background Check Robot 

 This robot will perform a background check for New Business License Application.

## Table of Content
* [Type](#type)
* [How it Works](#how-it-works)
* [How to Use](#how-to-use)

### Type

Robot

### How it Works

Robot will be triggered when clerk review the task. This Background Check Robot will perform below tasks:

* Search in social media like Linkedin, Twitter, Facebook and Instagram for company profiles.
* Validate the business website.
* Fetch google search results of the company.
* Validate if company is blacklisted or not.

Finally capture the screenshot of all the task and append it to a PDF file.

### How to Use

Below instructions can be followed to run the robot.

 * Build the robot using [Robocorp](https://robocorp.com/). Refer this [background-check](https://github.com/shibin-aot/formsflow-ai-extensions/tree/main/formsflow-robocorp-sample/camunda-springboot-external-client/external-client-extention/src/main/resources/robots/background-check) robot.
 * Add the robot to the path `{base-dir}/src/main/resources/robots`.
 * Make sure your current working directory is `{base-dir}/src/main/resources/robots/background-check`.
 * To Test if robot is working, run the below command:
 
 ```       
rcc run 
```  
