
# formsflow.ai Camunda RPA Client

![Java](https://img.shields.io/badge/Java-JDK_1.8-blue)  ![Windows/Linux](https://img.shields.io/badge/Windows/Linux-OS-blue)  ![Robocorp](https://img.shields.io/badge/Robocorp-rcc-blue)   ![postgres](https://img.shields.io/badge/postgres-latest-blue)  

Run your automation using the camunda rpa client



## Table of Content
1. [Introduction](#introduction)
2. [About the Project](#about-the-project)
   * [Features](#features)
3. [Download and Installation](#download-and-installation)
4. [System Architecture](#system-architecture)
5. [Quick Start](#quick-start)
6. [Usage](#usage)
7. [License](#license)
8. [Links](#links)

## Introduction

This is an extension provided by [formsflow.ai](https://formsflow.ai/) to enable RPA feature  to automate complex tasks.
Therefore, this application can be extended with camunda by enabling the external task handler from the service task and 
further collaborated with a Robot.

## About the Project

The project was initiated by [AOT Technologies](https://www.aot-technologies.com/)  as a means of addressing the complex
robotic use-cases which need to be automated outside [formsflow.ai](https://formsflow.ai/).

Typical use cases are:

* Browser task eg: web scraping.
* Tedious excel task eg: copying data from other sources to a given Excel file.
* PDF task eg: associating html pages / images to a PDF file.

### Features

* Builtin association with OpenSource RPA framework Robocorp.
* Configurable robot with audit and history.
* Support both cloud and command line robots.
* Input / Output operation supported.
* Supports multiple OS eg: Windows 10, Linux.
* Supports Docker build.

## Download and Installation

For download and setup, follow the [installation guide](../external-client-extention/INSTALLATION.md), you will find step-by-step instructions to download and install.

## System Architecture

![Architecture](https://user-images.githubusercontent.com/85665463/150078528-f05637b5-ae37-48c1-b4e4-501fb11c08d9.png)

## Quick Start

Following instructions need to be followed to run this project.

1. Make sure to have Camunda running.
2. Create a simple process model with an External Service Task and define the topic as 'topicName'
3. Deploy the process to the Camunda Platform engine.
4. Create a robot and associate it with the topic like in BackgroundCheckRobotHandler by creating a new handler.
5. Start the camunda-rpa-client app.
6. Log in to formsflow.ai web component and associate the workflow with a form and submit the form.
7. The robot will be triggered

## Usage

For usage instructions, Please refer [here](../external-client-extention/USAGE.md)
 
## About External Client

This project is an extension to camunda's own [External task client](https://github.com/camunda/camunda-bpm-platform/tree/master/clients/java) to run robots

This works based on an external task subscription, it triggers the robots using [rcc](https://robocorp.com/docs/rcc/installation).

On completion of the robot, the external task client will send the completion request with data if any to camunda.


## License

Copyright 2020 AppsOnTime-Technologies 2020

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


## Links
