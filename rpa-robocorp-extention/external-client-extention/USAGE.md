# Usage Instructions
In the following document, we’ll describe the step by step instructions to set up a working robot in the formsflow.ai solution.

## Table of Contents
1. [Usage Instructions](#usage-instructions)
    * [STEP 1 : Designer Steps](#designer-steps)
    * [STEP 2 : Client Steps](#client-steps)
    * [STEP 3 : Reviewer Steps](#reviewer-steps)
2. [Usage Instructions with examples](#usage-instructions-with-examples)
    * [Create Background Check Robot](#create-background-check-robot)
        * [STEP 1 : Designer Steps](#designer--steps)
        * [STEP 2 : Client Steps](#client--steps)
        * [STEP 3 : Clerk Steps](#clerk-steps)
        * [STEP 4 : Approver Steps](#approver-steps)
3. [Database Configuration](#database-configuration)
4. [Links](#links)

## Usage Instructions

The following instructions can be followed to use camunda rpa client solution after the successful healthcheck.

###  Designer Steps
  
 * Build a new robot using robot framework [Robocorp](https://robocorp.com/).
 * Robot can be deployed to cloud / place it under resources/robots to run it. 
 * Create a new Java handler to manage the robot and associate the robot with the handler using database configuration.  
 * Build a new process model using [Camunda Modeler](https://camunda.com/download/modeler/) with an External Service Task and deploy the process to formsflow.ai.
 * After the successful deployment, login to [forms-flow-web](http://localhost:3000) using designer credentials and associate the form with workflow. To learn more about how to associate the form with workflow, go to [link](https://github.com/AOT-Technologies/forms-flow-ai/blob/master/USAGE.md)
 * Now **Log out** and go to [Client Steps](#client-steps) 
 
### Client Steps

 * Login to [forms-flow-web](http://localhost:3000) with client credentials.
 * Fill and submit the form. To learn more about how to submit the form, Please refer [here](https://github.com/AOT-Technologies/forms-flow-ai/blob/master/USAGE.md)
 * Robot will be triggered.
 * Now **Log out** and go to [Reviewer Steps](#reviewer-steps)

### Reviewer Steps 

 * Login to  [forms-flow-web](http://localhost:3000) with reviewer credentials.
 * Review the task and reviewer can take an action based on robot response
 * Reviewer action is completed.
 
> Clerk & Approver are part of the main group Reviewer, so the steps are similar to Reviewer Steps.

## Usage Instructions with examples

## Create Background Check Robot

> The following instructions are using an example form from the [formsflow.ai](https://formsflow.ai/) - `Create New Business License Application`.

### Designer  Steps

 * Build the robot using OpenSource RPA framework [Robocorp](https://robocorp.com/). Refer this [example](https://github.com/robocorp/example-google-image-search) robot.
 * Add the robot to the path `{base-dir}/src/main/resources/robots`
 * Create a new handler to manage the robot under the package `org.camunda.rpa.client.handlers` `eg:BackgroundCheckRobotHandler`. Refer [How to Configure A Java Handler](./starter-examples/handlers/background-check-robot-handler-readme.md) to learn more about the detailed configuration.
 * Associate the robot with handler by using a simple database configuration. Refer [Database Configuration](#database-configuration) to learn the detailed configuration.
 * Create a new process model using [Camunda Modeler](https://camunda.com/download/modeler/) with an External Service Task - `Two Step Approval With RPA`

![image](https://user-images.githubusercontent.com/85665463/150779537-376a0394-74d4-4897-85a5-34b954048197.png)

 * Add `FormBPMDataPipelineListener` if you need to accept any parameters from form.io to Camunda RPA Client.To know more about `FormBPMDataPipelineListener` configuration, Please refer [here](https://github.com/AOT-Technologies/forms-flow-ai/blob/master/forms-flow-bpm/starter-examples/listeners/formbpmdatapipeline-readme.md)
 * Add topic name `background-check` to the property panel of `Complete Background Verification` Service Task.
 
![image](https://user-images.githubusercontent.com/85665463/150779740-ed1699f2-c325-441f-84a0-c4b7acdd6b8f.png)

 * Now deploy the process to Camunda Platform Engine.
 * Login to [forms-flow-web](http://localhost:3000) using designer credentials and associate the `New Business License Application` form with `Two Step Approval With RPA` workflow. To learn more about how to associate the form with workflow, go to [link](https://github.com/AOT-Technologies/forms-flow-ai/blob/master/USAGE.md)    
 * Now **Log out** and go to [Client Steps](#client--steps)

### Client  Steps


 * Login to [forms-flow-web](http://localhost:3000) with client credentials.
 * Submit the `New Business License Application` in formsflow.ai. To learn more about how to submit the form, Please refer [here](https://github.com/AOT-Technologies/forms-flow-ai/blob/master/USAGE.md)
 * Fill in the details and submit the form, `background-check` robot will be triggered and perform its task.
 * Now **Log out** and go to [Clerk Steps](#clerk-steps)
   
### Clerk Steps

 * Login to [forms-flow-web](http://localhost:3000) with clerk credentials.
 * The associated task can be found for the Clerk role in the **Task Tab** section
 * Click on the **Task tab** to claim and review the task.
 * You can view and download the robot response from the form.
 * Clerk can take an action based on robot response.
 * Log out and go to [Approver Steps](#approver-steps).

### Approver Steps

 * Login to  [forms-flow-web](http://localhost:3000) with reviewer credentials.
 * Click on the **Task tab** to claim and review the task.
 * Submit his/her review with feedback. Then the status change to approved.
 * Reviewer action is completed.

## Database Configuration

BackgroundCheckRobotHandler make use of Robot Handler Configuration to associate with the robot. Robot Handler configuration Table includes below fields.

* **`handlerId`** - Robot is identified by a unique `HANDLER_ID` in the configuration table. This `HANDLER_ID` is referred from the robot handler class.

* **`robotName`** - Name of the Robot. Robot name should be similar to the robot folder name added to `{base-dir}/src/main/resources/robots` path. `eg: background-check` 

* **`topicName`** - Topic name of the External Service Task. This should be similar to the robot folder name. Topic name is referred to `@ExternalTaskSubscription` annotation in handler class and `Complete Background Verification` service task in the workflow. `eg:background-check` 

* **`variableNames`** - Robot Input variables will be in comma seperated format.

* **`taskFileName`** - Represents Task File Name. `eg: task.robot, task.py`

* **`processDefinitionKey`** - Filter for External Tasks with this process definition key

* **`responseType`** - This is referred to the response type of robot. `eg: FILE, FILE_MULTI, KEY_VALUE`

* **`robotType`** - This is referred to the type of robot. `eg: ROBOCORP_RCC, ROBOCORP_CLOUD`

* **`workspace_id`** - If the robot is deployed in robocorp cloud provide the workspace id.
  
* **`processId`** - If the robot is deployed in robocorp cloud provide the robot process id.

* **`description`** - Defines the description of robot.

* **`createdDate`** - Date and time of robot.


## Links

* [Web site](https://formsflow.ai/)
* [Youtube Link](https://youtu.be/_H-P3Av3gqg)
* [Camunda External Task Client](https://camunda.com/blog/2021/03/external-task-client-spring-bootified/)
* [Robocorp RPA](https://robocorp.com/)
* [RCC toolchain](https://robocorp.com/docs/rcc/overview)
