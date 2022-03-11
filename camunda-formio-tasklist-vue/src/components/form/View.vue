<template>
    <!-- don't remove form-render class -->
    <Form
    v-if="formioUrl"
    :src="formioUrl"
    :options="{ readOnly: true }"
    class="form-render"
    >
    </Form>
</template>

<script lang="ts">
import "semantic-ui-css/semantic.min.css";
import "formiojs/dist/formio.full.min.css";
import {
  Component, Prop, Vue 
} from "vue-property-decorator";
import {
  Form 
} from "vue-formio";

@Component({
  components: {
    Form
  },
})
export default class FormView extends Vue {
    @Prop() private formioUrl!: string;
}
</script>
