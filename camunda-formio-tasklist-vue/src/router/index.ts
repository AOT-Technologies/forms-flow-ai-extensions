import VueRouter, {
  RouteConfig 
} from 'vue-router';


import Vue from 'vue';

Vue.use(VueRouter);



const routes: Array<RouteConfig> = [
  {
    path: '/:getTaskId?',
    name: 'Tasklist',
    component: () => import('@/views/TaskList.vue'),
    props: true
  },
  {
    path: '/form/:formId/submission/:submissionId',
    name: 'Formio forms list',
    component: () => import('@/components/FormViewSubmission.vue')
  }
];

const router = new VueRouter({
  mode: 'history',
  routes
});

export default router;
