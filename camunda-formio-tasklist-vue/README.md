# camunda-formio-tasklist-vue

<p align="center"><img src=".images/logo.png"/></p> 

This is a Vue component build for formsflow.ai solution to integrate
Camunda Tasklist, associated forms in a bundled solution which can
be installed as a package. 


## Things to look out when integrating


- Ensure you import the appropriate Vue component defined in [TaskList View](https://github.com/AOT-Technologies/forms-flow-ai-extensions/blob/master/camunda-formio-tasklist-vue/src/views/TaskList.vue) and do the environment configuration in the integrated solution.

- In the Vue project's `main.ts` file which you are integrating, add
the following code to use our Vuex modules:

```
import FormsFlowStore from 'camunda-formio-tasklist-vue/src/store/index'
Vue.use(FormsFlowStore, { store })
```

- Write a seperate router in integration project for defining routers for
Application History `view submission` using our component to view
the Form.


## Local Project setup
```
npm install
```

## Compiles and hot-reloads for development
```
npm run serve
```

## build for production with minification
```
npm run build
```

## build for production and view the bundle analyzer report
```
npm run build --report
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


