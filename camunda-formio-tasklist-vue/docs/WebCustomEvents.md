## forms-flow-web Vue Events 

 > This section elaborates  events used in forms-flow-web.
 >  The Form.io renderer uses the [EventEmitter3](https://github.com/primus/eventemitter3) library to manage all of the event handling that occurs within the renderer. Within the Form.io renderer, there are a number of events that are fired that allow you to respond to these events and then do something once they fire.

## :zap: Events

| Name | Description  &nbsp;&nbsp;&nbsp;| Arguments &nbsp;&nbsp;&nbsp; | Example |
| --- | --- | --- |--- |
| `reloadTasks` | Triggered for button components configured as Event action. The task is removed and refreshes RHS . Used in the task page.  | <li> type:The configured event type </li> |form.emit('customEvent', {  type: "reloadTasks"}); |
| `reloadCurrentTask` | Triggered for button components configured as Event action.It refreshes the task page |<li>type:The configured event type</li>|form.emit('customEvent', { type: "reloadCurrentTask"}); |
| `customSubmitDone` | Similar to "submit" but only fires when the submission is "saved" via API and redirect to Form list page. Used in the create form page ||form.emit('customEvent', {type: "customSubmitDone"}); |
| `actionComplete` | Triggered for button components configured as Event action. |<li>type:The configured event type</li><li>component:The component json</li><li>actionType: Appove/Reject</li> | form.emit('customEvent', { type: "actionComplete",    component: component, actionType:data.actionType }); |
