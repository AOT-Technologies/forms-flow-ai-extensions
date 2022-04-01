<template>
       <div v-if="valueLabelArray.length" class="d-flex flex-wrap justify-content-between">
        <div 
        v-for="(item, index) in valueLabelArray"
        :key="index"
        class="col-sm-12 col-lg-6 p-0 px-1"
       >
        <div class="my-2">
        <div class="text-truncate ">
        <span style="font-weight:bold; font-size:0.9rem">
           {{item.label}}
        </span>
        </div>
        <div class="text-truncate ">
          <span data-toggle="tooltip"  class="ctf-description" data-placement="top" :title="checkVlaueIsDateOrNOt(item.value)">
         {{ checkVlaueIsDateOrNOt(item.value)}}  
          </span>
        </div>
        </div>
       </div>
       <div class="w-100 text-center btn-link"  v-if="variables.length > 2"  @click.stop="toggleShowMore" >
       <i
       class="fa fa-angle-down"
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
  Watch,
} from "vue-property-decorator";
import {
  getFormattedDateAndTime
} from "../../services";
@Component
export default class TaskVariable extends Vue {
    @Prop() private variables!: any[];
    @Prop() private filterTaskVariable: any;
    private showMore: boolean = false;
    private variableCount =0;
    private  valueLabelArray: Array<any>= [] ;
    toggleShowMore(){
      this.showMore= !this.showMore;
    }

    mounted(){
      this.returningData();
    }

    @Watch('showMore')
    updateData(){
      this.returningData();
      if(this.showMore){
        this.variableCount=0;
      }
    }

    returningData(){
      if(this.filterTaskVariable){
        const newArray: any =[];
        this.filterTaskVariable.forEach((taskItem)=>{
          const data = this.variables.find(variableItem=> variableItem.name===taskItem.name);
          if(data&&data.value!==(undefined || null)){
            if( this.variableCount<2){
              this.variableCount++;
              newArray.push({
                label:taskItem.label,value:data.value
              });
            }else if(this.showMore){
              newArray.push({
                label:taskItem.label,value:data.value
              });

            } 
          } 
        });
        this.valueLabelArray= newArray;
      }
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
