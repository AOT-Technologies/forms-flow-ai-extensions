# camunda-formio-tasklist-vue

![image](https://user-images.githubusercontent.com/70306694/124284590-c3078b80-db6a-11eb-81c7-921cfbe46495.png)

This is a Vue component build for formsflow.ai solution to integrate
Camunda Tasklist, associated forms in a bundled solution which can
be installed as a package. 


## Things to look out when integrating

- Install the latest version of [Camunda-formio-tasklist-vue](https://www.npmjs.com/package/camunda-formio-tasklist-vue) and [bootstrap-vue](https://bootstrap-vue.org/)
- Ensure you import the appropriate Vue component as defined in [TaskList View](https://github.com/AOT-Technologies/forms-flow-ai-extensions/blob/master/camunda-formio-tasklist-vue/src/views/TaskList.vue) and do the environment configuration in the integrated solution.
- In your `main.ts` remember to import bootstrap-vue dependencies as [this example project](https://github.com/ann-aot/service-flow-vue/blob/main/src/main.ts).
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

## Required Environmental Variables

| Variable | Description |
| --- | --- |
|`KEYCLOAK_URL`| The URL of the Keycloak server. |
|`KEYCLOAK_URL_REALM`| The realm of the Keycloak server. |
|`KEYCLOAK_CLIENT_ID`| The client ID of the client that will be used to authenticate against Keycloak. |
| `BPM_URL` | Specifies the Camunda Rest API URL. |
|`FORM_IO_API_URL` | The URL of the form.io server |
|`FORM_IO_RESOURCE_ID`| User forms form-Id |
|`FORM_IO_REVIEWER_ID`| forms-flow-forms administrator role Id |
|`FORM_IO_REVIEWER`| The role name used for designer users. Default value is `formsflow-designer` |
|`FORM_FLOW_API_URL`| The URL of the form flow API  service. |
|`FORMIO_ROLES`| The valid FORMIO_ROLES as part of formio service. |

## License

Code released under the [Apache License, Version 2.0](./LICENSE).
