<template>
    <!-- don't remove form-render class -->
    <Form
    v-if="formioUrl"
    :src="formioUrl"
    :options="{noAlerts: false, readOnly: false}"
    v-on:submit="onFormSubmitCallback"
    v-on:customEvent="oncustomEventCallback"
    class="form-render"
    >
    </Form>
</template>

<script lang="ts">
import "formiojs/dist/formio.full.min.css";
import {
  Component, Emit, Prop, Vue 
} from "vue-property-decorator";
import {
  Form 
} from "vue-formio";

@Component({
  components: {
    Form
  },
})
export default class FormEdit extends Vue {
    @Prop() private formioUrl!: string;

    @Emit("onformsubmit")
    onFormSubmitCallback (item: any) {
      return item;
    }

    @Emit("oncustomevent")
    oncustomEventCallback (item: any) {
      return item;
    }
}
</script>
