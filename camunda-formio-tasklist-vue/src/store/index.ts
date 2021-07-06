import serviceFlowModule from "./modules/serviceFlow-module";

// export default function install (Vue: any, options: any) {
//   options.store.registerModule('serviceFlowModule', serviceFlowModule)
// }
export default function install(store: any) {
  store.registerModule("serviceFlowModule", serviceFlowModule);
}
