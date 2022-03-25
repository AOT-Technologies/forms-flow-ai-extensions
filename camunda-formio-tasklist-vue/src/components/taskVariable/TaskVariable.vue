<template>
       <div class="d-flex flex-wrap justify-content-between">
        <div 
        v-for="(item, index) in variables"
        :key="index"
        class="col-sm-12 col-lg-6 p-0 px-1"
       >
        <div class="my-2" v-if="(showMore||index < 2) && item.value!==undefined">
        <span style="font-weight:bold; font-size:0.9rem">
           {{filterTaskVariable[item.name]}}
        </span>
        <div class="text-truncate ">
          <span data-toggle="tooltip"  class="ctf-description" data-placement="top" :title="checkVlaueIsDateOrNOt(item.value)">
         {{ checkVlaueIsDateOrNOt(item.value)}}  
          </span>
        </div>
        </div>
       </div>
       <div class="w-100 text-center "  v-if="index > 2" >
       <i
      
       class="fa fa-angle-down"
       @click.stop="toggleShowMore"
       :style="{transform: `${showMore ? 'rotate(180deg)' : 'rotate(0deg)'}`}"
         data-toggle="tooltip" 
         title="show more"
         aria-hidden="true"
       />
     </div>
       </div>
</template>

<script lang="ts">

import {
  Component,
  Prop,
  Vue,
} from "vue-property-decorator";
import {
  getFormattedDateAndTime
} from "../../services";
@Component
export default class TaskVariable extends Vue {
    @Prop() private variables!: any[];
    @Prop() private filterTaskVariable;
    private showMore: boolean = false;

    toggleShowMore(){
      this.showMore= !this.showMore;
    }


    checkVlaueIsDateOrNOt=(value: any)=>{
      const isValueNumber = isNaN(value);
      if(isValueNumber){
        return getFormattedDateAndTime(value)!=='Invalid Date'?getFormattedDateAndTime(value):value;
      }else{
        return value;
      }
    }
 
}
</script>
<style scoped>
  .ctf-description {
    opacity: 0.6;
    font-size: 0.9rem;
  }
</style>
