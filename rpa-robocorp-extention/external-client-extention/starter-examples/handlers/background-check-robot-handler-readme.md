# Background Check Robot Handler

**org.camunda.rpa.client.handlers.impl.BackgroundCheckRobotHandler**

 This component can be used to handle and manage the robot.

## Table of Content
* [Type](#type)
* [How it Works](#how-it-works)
* [How to Configure](#how-to-configure)

### Type

Java Handler

### How it Works

This component will manage the robot to build a script with input parameters and run the robot.

### How to Configure

Below instructions can be followed to configure a robot handler to manage your robot.

* Subscribe the Handler to a topic with `@ExternalTaskSubscription` annotation and `topic name`. `eg:@ExternalTaskSubscription("background-check")`

* The robot Handler implements `IRobotHandler` interface and `ExternalTaskHandler` interface. 

* Override the below methods and provide business logics. These methods are used to manage and run the robot successfully.

  * **`startAudit`** - Create a new entry in the Audit table to acknowledge that the robot started successfully. This method invokes `org.camunda.rpa.client.core.RobotDataManager#initAudit()` to update the robot current status `eg:IN_PROGRESS` 
  
  * **`buildInputWithAdditionalVariables`** - This method will build input with additional variables for robot by invoking the `org.camunda.rpa.client.core.RobotIOManager#buildInput()`.
  
  * **`buildInput`** - This is an overriden method expecting the parameter `ExternalTask` and `RobotHandlerConfig` to build the Input.
  
  * **`runRobot`** - This method invokes `org.camunda.rpa.client.core.RobotDataManager#runRobot()` to find out the robot type, build a script with input parameters and run using script manager.
  
  * **`collectResponse`** - Once robot completed its task successfully, this method invokes `org.camunda.rpa.client.core.RobotIOManager#getOutputData` to collect the response. 
    
  * **`completeAudit`** - Complete the audit process by invoking `org.camunda.rpa.client.core.RobotDataManager#finalizeAudit` and update the status in audit table. `eg:SUCCESS/FAILURE`

  * **`handleFailure`** - Reports a failure to execute a task. A number of retries and a timeout untilthe task can be specified. If the retries are set to 0, an incident for thistask is created.

  * **`doCleanup`** - This function will clear the robot current output directory and it's data
 
  * **`getHandlerId`** - Each Robot handler is identified by a unique `HANDLER_ID`.

* Checkout the configurations for [BackgroundCheckRobotHandler](../../src/main/java/org/camunda/rpa/client/handlers/impl/BackgroundCheckRobotHandler.java).
