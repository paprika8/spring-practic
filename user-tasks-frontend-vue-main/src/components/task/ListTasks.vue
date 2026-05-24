<template>
  <div class="container-md mt-3">
    <div class="row">
      <div class="col-sm-4 mt-2">
        <div class="list-group">
          <div v-for="(task, index) in tasks" :key="task.id" class="list-group-item">
            {{ index + 1 }}. {{ task.title }} ({{ task.description }})
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "../../http-common";

export default {
  name: "ListTasks",
  data() {
    return {
      tasks: [],
    };
  },
  methods: {
    listTasks() {
      http.get("/tasks")
          .then(response => {
            this.tasks = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
  },
  mounted() {
    this.listTasks();
  },
};
</script>
