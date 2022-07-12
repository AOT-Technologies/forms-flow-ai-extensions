import 'bootstrap';
import "bootstrap/dist/css/bootstrap.min.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import App from "./App.vue";

import Vue from "vue";
import router from "./router";
import store from "./store/index";

Vue.config.productionTip = false;


new Vue({
  router,
  store,
  render: (h: any) => h(App),
}).$mount("#app");
