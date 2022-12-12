# camunda-formio-tasklist-vue


[![Join the chat at https://gitter.im/forms-flow-ai/community](https://badges.gitter.im/forms-flow-ai/community.svg)](https://gitter.im/forms-flow-ai/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) 
[![Join the chat at https://stackoverflow.com/questions/tagged/formsflow.ai](https://img.shields.io/badge/ask%20-on%20%20stackoverflow-F47F24)](https://stackoverflow.com/questions/tagged/formsflow.ai?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
<img src="https://img.shields.io/badge/LICENSE-Apache%202-green"/>

![image](https://user-images.githubusercontent.com/70306694/124284590-c3078b80-db6a-11eb-81c7-921cfbe46495.png)

This is an NPM package for using the formsflow.ai solution. It can be used
as a  Vue component build to integrate Camunda Tasklist, associated
forms in a bundled solution.  

:inbox_tray: You can install it [NPM package manager](https://www.npmjs.com/package/camunda-formio-tasklist-vue).


## :information_source: Things to look out for when integrating 

- Install the latest version of [Camunda-formio-tasklist-vue](https://www.npmjs.com/package/camunda-formio-tasklist-vue) and [bootstrap-vue](https://bootstrap-vue.org/)
- Ensure you import the appropriate Vue component as defined in [TaskList View](https://github.com/AOT-Technologies/forms-flow-ai-extensions/blob/master/camunda-formio-tasklist-vue/src/views/TaskList.vue) and do the environment configuration in the integrated solution.
- In your `main.ts` remember to import bootstrap-vue dependencies.
[Refer to this example project](https://github.com/AOT-Technologies/service-flow-vue/blob/main/src/main.ts).
- Write a separate router in the integration project for defining routers for
Application History `view submission` using our component to view
the Form.
- Define CSS variable in your file `--bs-primary` and give some color to your buttons. example
 ` :root {  --bs-primary: #0d6efd;} `

A sample project which was used for integration `camunda-formio-tasklist-vue`
components can be found :point_right: [here](https://github.com/AOT-Technologies/service-flow-vue/).

## :sparkles: Components 

**TaskList component** -  The customizable component which can be
used to integrate formsflow.ai solution in your Vue.js based project.

:beginner: [Documentation for TaskList component](https://github.com/AOT-Technologies/forms-flow-ai-extensions/blob/master/camunda-formio-tasklist-vue/docs/components/TaskList.md)

**FormViewSubmission component** - The component for viewing the Forms Submission.

:beginner: [Documentation for FormViewSubmission component](https://github.com/AOT-Technologies/forms-flow-ai-extensions/blob/master/camunda-formio-tasklist-vue/docs/components/FormViewSubmission.md)

You can checkout the available **formio custom events** which can be used in camunda-formio-tasklist-vue [here](./docs/WebCustomEvents.md)

## :oncoming_taxi: Project Setup 

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

If you are interested in contributing to the project :heart_eyes:, do checkout our [style guide to follow project guidelines](./docs/StyleGuide.md)


## :warning: License 

Code is released under the [Apache License, Version 2.0](./LICENSE). 
