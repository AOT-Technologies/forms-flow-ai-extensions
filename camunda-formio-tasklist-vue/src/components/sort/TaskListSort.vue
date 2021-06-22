<template>
  <div class="cft-first">
    <div id="cftf-dpdown-container" class="mx-2">
      <div
        class="cftf-dpdown-box mr-2"
        v-for="(sort, idx) in sortList"
        :key="sort.sortBy"
      >
        <b-row>
          <b-col cols="1" v-b-tooltip.hover title="Remove Sorting Field">
            <span
              v-if="sortList.length > 1"
              class="cftf-exit-button"
              @click="deleteSort(sort, idx)"
            >
              <i class="fa fa-times"></i>
            </span>
          </b-col>
          <b-col cols="7">
            <b-nav-item-dropdown
              :text="sortList[idx].label"
              v-b-tooltip.hover
              title="Click To Change Field for Sorting"
              :no-caret="true"
            >
              <span v-if="sortOptions.length">
                <b-dropdown-item-button
                  v-for="sort in sortOptions"
                  :key="sort.sortBy"
                  @click="updateSort(sort, idx)"
                  >{{ sort.label }}
                </b-dropdown-item-button>
              </span>
              <b-dropdown-item-button v-else
                >Sorry, no more fields to sortBy.</b-dropdown-item-button
              >
            </b-nav-item-dropdown>
          </b-col>
          <b-col cols="1" v-b-tooltip.hover title="Toggle Sort Order">
            <a
              v-if="sort.sortOrder === 'asc'"
              @click="toggleSort(idx)"
              href="#"
              title="Ascending"
            >
              <i class="fa fa-chevron-up cftf-arrow" aria-hidden="true"></i>
            </a>
            <a v-else @click="toggleSort(idx)" href="#" title="Descending">
              <i class="fa fa-chevron-down cft-arrow" aria-hidden="true"></i>
            </a>
          </b-col>
        </b-row>
      </div>
      <TaskSortOptions
        :sortOptions="sortOptions"
        :updateSortOptions="updateSortOptions"
        @add-sort="addSort"
      >
      </TaskSortOptions>
    </div>
  </div>
</template>

<script lang="ts">
export default {};
  getOptions (options: any) {
    const optionsArray: {
      sortOrder: string;
      label: string;
      sortBy: string;
    }[] = [];
    sortingList.forEach((sortOption) => {
      if (
        !options.some(
          (option: { sortBy: string }) => option.sortBy === sortOption.sortBy
        )
      ) {
        optionsArray.push({ ...sortOption });
      }
    });
    return optionsArray;
  }

  addSort (sort: any) {
    this.sortList.push(sort);
    if (this.sortList.length === sortingList.length) {
      this.updateSortOptions = this.sortOptions;
      this.sortOptions = [];
    } else {
      this.sortOptions = this.getOptions(this.sortList);
    }
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

  updateSort (sort: any, index: number) {
    this.sortList[index].label = sort.label;
    this.sortList[index].sortBy = sort.sortBy;

    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

  deleteSort (sort: any, index: number) {
    this.sortList.splice(index, 1);
    this.updateSortOptions = [];
    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

  toggleSort (index: number) {
    if (this.sortList[index].sortOrder === "asc")
      this.sortList[index].sortOrder = "desc";
    else {
      this.sortList[index].sortOrder = "asc";
    }
    this.payload["sorting"] = this.sortList;
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

</script>
