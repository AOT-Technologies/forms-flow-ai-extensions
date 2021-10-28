# Changes for camunda-formio-tasklist-vue

## v1.0.14, 2021-10-25 

#### Added

- Add new props `taskSortBy` and `taskSortOrder` to change default task sorting options 
- Add new prop `containerHeight` to adjust the height values of Vue component, in case of Vue component is too much for your integrated application
- Allow authorisation to tasklist based on `formioReviewer` prop

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
- Multiple reloading of Tasklist on user actions.
- Flicking of screen when the task is being claimed/unclaimed
- Tasklist LHS flicking when multiple forms are being created.
- On creating new tasks, LHS of tasklist not being updated.

## v1.0.9, 2021-07-15

- Added dynamic store registration of the npm component for easy Integration. So on integration, there is no need to register out the store in their main.ts.
- Fix Allignment issues in TaskDetail menu
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
- Fix Tasklist search bugs

## v1.0.4, 2021-05-13

- Tasklist Claim user list (support for more than 250+ users)
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
