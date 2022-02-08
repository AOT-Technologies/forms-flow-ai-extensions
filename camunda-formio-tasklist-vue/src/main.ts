import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import App from "./App.vue";
import {
  Formio
} from 'vue-formio'
import {
  AppConfig
}from "./services"
import {
  BootstrapVue
} from "bootstrap-vue";
import Vue from "vue";
import router from "./router";
import store from "./store/index";
Formio.setProjectUrl(AppConfig.porjectUrl);
Formio.setBaseUrl(AppConfig.apiUrl);
Vue.config.productionTip = false;

Vue.use(BootstrapVue);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
