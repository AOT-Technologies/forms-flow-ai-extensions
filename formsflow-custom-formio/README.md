# formsflow-formio-custom-elements

This is a collection of custom FORMIO components and templates for Formsflow.ai project.

## Build

1) To create Build with all dependencies

```
npm run build
```
Output file name : `customformio.js`

2) To create build by excluding form-io dependencies so that it can resolve it from consumer application.

```
npm run buildExcludeFormio
```
Output file name : `customformio-ex.js`
### Consumer projects

## Formsflow.ai API in forms-flow-ai\forms-flow-api\src\formsflow_api\templates\index.html

Including direct js
```
   <script src="https://unpkg.com/formsflow-formio-custom-elements/dist/customformio.js"></script>
```

## Formsflow.ai WEB in forms-flow-ai\forms-flow-web\src\index.js
```
npm install @aot-technologies/formsflow-formio-custom-elements --save
```

```Javascript
import { Components } from "react-formio";

// Set custom formio elements - Code splitted
import("@aot-technologies/formsflow-formio-custom-elements/dist/customformio-ex").then(
  (FormioCustomEx) => {
    Components.setComponents(FormioCustomEx.components);
  }
);
```