<template>
  <div class="table-footer-pagination">
    <div>
      <small>Count:</small> <div class="badge rounded-pill bg-primary">{{ totalRecords }}</div>
    </div>
    <ul class="pagination pagination-sm">
      <li
        class="page-item"
        @click="goToPage(1)"
      >
        <a
          class="page-link"
          href="#"
          aria-label="first"
        >
          <i class="far fa-angle-double-left"></i>
        </a>
      </li>
      <li
        class="page-item"
        :class="{'active': currentPageNumber === index}"
        v-for="index in totalPageNumbers"
        :key="index"
        @click="goToPage(index)"
      >
        <a
          v-if="(index <= (currentPageNumber + 2)) && index >= (currentPageNumber - 2)"
          class="page-link"
          href="#"
        >{{ (index === (currentPageNumber + 2)) || (index === (currentPageNumber - 2)) ? '...' : index }}</a>
      </li>
      <li
        class="page-item"
        @click="goToPage(totalPageNumbers)"
      >
        <a
          class="page-link"
          href="#"
          aria-label="last"
        >
          <i class="far fa-angle-double-right"></i>
        </a>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import {
  Component, Emit, Prop, Vue,
} from 'vue-property-decorator';

@Component({
})
export default class Pagination extends Vue {
  @Prop(Number)
  totalRecords!: number;

  @Prop(Number)
  perPage!: number;

  currentPageNumber = 1;

  get totalPageNumbers() {
    return Math.ceil(this.totalRecords / this.perPage);
  }

  @Emit()
  goToPage(page: number) {
    this.currentPageNumber = page;
    return this.currentPageNumber;
  }
}
</script>

<style lang="scss" scoped>
.table-footer-pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
  .page-size {
    display: flex;
    align-items: center;
    align-content: center;
    .form-control {
      width: 48px;
      height: 38px;
      margin: 0 8px;
    }
    /* Chrome, Safari, Edge, Opera */
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }

    /* Firefox */
    input[type="number"] {
      -moz-appearance: textfield;
    }
  }
  .pagination {
    justify-content: flex-end;
    margin-bottom: 0;
  }
}
</style>
