# camunda-formio-tasklist-vue

![image](https://user-images.githubusercontent.com/70306694/124284590-c3078b80-db6a-11eb-81c7-921cfbe46495.png)

This is a NPM package for formsflow.ai solution. It can be used
as a  Vue component build for to integrate Camunda Tasklist, associated
forms in a bundled solution.  You can install it :inbox_tray: [NPM package manager](https://www.npmjs.com/package/camunda-formio-tasklist-vue).


## Things to look out when integrating

- Install the latest version of [Camunda-formio-tasklist-vue](https://www.npmjs.com/package/camunda-formio-tasklist-vue) and [bootstrap-vue](https://bootstrap-vue.org/)
- Ensure you import the appropriate Vue component as defined in [TaskList View](https://github.com/AOT-Technologies/forms-flow-ai-extensions/blob/master/camunda-formio-tasklist-vue/src/views/TaskList.vue) and do the environment configuration in the integrated solution.
- In your `main.ts` remember to import bootstrap-vue dependencies.
[Refer this example project](https://github.com/AOT-Technologies/service-flow-vue/blob/main/src/main.ts).
- Write a seperate router in integration project for defining routers for
Application History `view submission` using our component to view
the Form.

:information_source: A sample project which was used for integration `camunda-formio-tasklist-vue`
components can be found :point_right: [here](https://github.com/AOT-Technologies/service-flow-vue/).

## Components in camunda-formio-tasklist-vue

### TaskList component

The customizable component which can be used to integrate formsflow.ai
solution in your Vue.js based project.

:beginner: [Documentation for TaskList component](./docs/components/TaskList.md)

### FormViewSubmission component

The component for viewing the the Forms Submission.

:beginner: [Documentation for TaskList component](./docs/components/FormViewSubmission.md)

## Project Setup

### Local Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### build for production with minification
```
npm run build
```

### build for production and view the bundle analyzer report
```
npm run build --report
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


## License

Code is released under the [Apache License, Version 2.0](./LICENSE). :warning:
