# Changes for camunda-formio-tasklist-vue

## v1.1.1, 2022-03-28

#### Modified

- Add process variable on filter task
- Updated groups UI
- Updated authorization API

#### Fixes 

- Highlighting Process Diagram
- On submitting new forms, LHS of TaskList not being updated
- Task variable null check
- Buffer function 
- Filter Task bugs 
- Follow up date and Due date bugs on Task details

## v1.1.0, 2022-03-11

#### Modified

- Add Task Variable on task list 
- Add hide task details prop feature 
- Add disable component feature
- Updated FORMIO details from multiple line Props to combined Props as a single Object
- Updated UI
- Add `taskDefaultFilterListNames` to default filter the Task List

## v1.0.19, 2022-02-25

#### Modified

- View submission history issues fixed 
- Add loaders for fetching TaskList and TaskDetail page
- Fix Claim Task assignee search filter bug
- Updated with Formio Url (BaseUrl, ProjectUrl) 

## v1.0.18, 2022-01-21

#### Modified

- authToken not updating after x minutes (patch release to fix issues in v1.0.17)

## v1.0.17, 2022-01-19

#### Modified

- Add new prop `formioJWTSecretKey` to pass the secret key used in forms-flow-forms server jwt secret.

## v1.0.16, 2022-01-12

#### Modified

- Task list count being refreshed correctly when a submission is complete.
- Dynamically Highlighting Process Diagram.
- Tooltip hover corrections

## v1.0.15, 2021-12-13

#### Modified

- Switching task list APIs to load with application/hal+json
- Fix issue of process Diagram First click showing blank diagram
- Add support of custom events for client
- Fix TaskList search bugs

## v1.0.14, 2021-10-25

#### Added

- Add new props `taskSortBy` and `taskSortOrder` to change default task sorting options
- Add new prop `containerHeight` to adjust the height values of Vue component, in case of Vue component is too much for your integrated application
- Allow authorization to TaskList based on `formioReviewer` prop

#### Modified

- TaskList Performance optimisation
- Remove redundant API calls
- Claim user SearchBy implemented using API calls

## v1.0.12, 2021-08-27

#### Fixes

- Task Search option to search by last Name, first Name and Email
- Generic UI updates
- FormList sorting by Form Name
- New container height attribute to dynamically adjust the height of the container

## v1.0.10, 2021-07-28

#### Fixes

- Form data takes a long time to load.
- Task being selected when loading initially.
- Screen flicking issue on reloading web socket calls.
- Task Email functionality not working.
- Multiple reloading of TaskList on user actions.
- Flicking of screen when the task is being claimed/unclaimed
- TaskList LHS flicking when multiple forms are being created.
- On creating new tasks, LHS of TaskList not being updated.

## v1.0.9, 2021-07-15

- Added dynamic store registration of the npm component for easy Integration. So on integration, there is no need to register out the store in their main.ts.
- Fix allignment issues in TaskDetail menu
- Fix tooltip issues
- Make camunda API calls async

## v1.0.7, 2021-06-30

- Unpublished  v1.0.6 due to duplicated  file issue

## v1.0.6, 2021-06-30

- Web Socket Reconnect Issue fix
- Vue upgrade to 2.6.14 version
- Claim user (Search by Last Name)
- Alignment corrections around claim user icon and assignee.
- Replace Expand/Contract icons with arrows icons in font-awesome
- Add PR template
- Remove Bootstrap-icons package
- Used bootstrap tooltip component for hover changes

## v1.0.5, 2021-05-19

- Full-Screen support for Task Detail menu
- Task Email support fixes issues on the TheQ side
- TaskList sort outside click fix
- Fix TaskList search bugs

## v1.0.4, 2021-05-13

- TaskList Claim user list (support for more than 250+ users)
- Claim User menu with Vue-select library
- Task Email support

## v1.0.3, 2021-05-08

- TaskList search outside click issue solved

## v1.0.2, 2021-05-03

- Task Search Functionality
- Task History Functionality
- Fix the Authorisation issue for the claim button
- Update project dependencies versions Bootstrap-icons and Vue-date picker issues
- Fix Cosmetic issues
- Remove breaking CSS changes
- Add hover details for removing the assignee button

## v1.0.1, 2021-03-31

- Task Sorting Functionality
- Create FormList Component
- TaskDetail section
- Claim userList

## v1.0.0, 2021-03-25

- Initial release
