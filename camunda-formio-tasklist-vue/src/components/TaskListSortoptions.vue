<template>
    <b-nav-item-dropdown v-if="updateSortOptions.length===0">
        <template slot="button-content">
            <i class="fa fa-plus fa-sm click-element cftf-add-sorting"/>
        </template>
        <b-dropdown-item-button
          v-for="s in sortOptions"
          :key="s.sortBy"
          @click="addSort(s)"
        >
          {{s.label}}
        </b-dropdown-item-button>
    </b-nav-item-dropdown>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue } from 'vue-property-decorator';

@Component
export default class TaskSortOptions extends Vue {
    @Prop() private sortOptions !: any;
    @Prop({default: 'true'}) private showSortListDropdown !: boolean;
    @Prop() private updateSortOptions !: any;

    @Emit()
    addSort(sort: any){
      return sort
    }

}
</script>
