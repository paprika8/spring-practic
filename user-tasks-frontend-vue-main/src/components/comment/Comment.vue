<template>
  <div v-if="comment" class="container-md mt-3">
    <div class="col-sm-6">
      <h4>Комментарий</h4>
      <p class="text-muted mb-3">
        {{ comment.username }} · {{ formatDate(comment.timestamp) }} ·
        <router-link :to="'/task/' + comment.taskId">Задача #{{ comment.taskId }}</router-link>
      </p>
      <form @submit.prevent="updateComment">
        <div class="form-group mb-3">
          <textarea
              v-model="comment.content"
              class="form-control"
              rows="4"
              placeholder="Текст комментария"
              required
          />
        </div>
        <div class="btn-group">
          <button type="submit" class="btn btn-success rounded">Обновить</button>
          <button type="button" class="btn btn-danger mx-1 rounded" @click="deleteComment">
            Удалить
          </button>
          <router-link :to="'/task/' + comment.taskId" class="btn btn-secondary rounded">
            К задаче
          </router-link>
        </div>
      </form>
      <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">{{ errorMessage }}</div>
    </div>
  </div>
  <div v-else class="container-md mt-3">
    <p>Комментарий не найден</p>
    <router-link to="/listTasks">К списку задач</router-link>
  </div>
</template>

<script>
import http from "../../http-common";

export default {
  name: "comment-details",
  props: ["id"],
  data() {
    return {
      comment: null,
      errorMessage: ""
    };
  },
  methods: {
    formatDate(value) {
      if (!value) {
        return "";
      }
      return new Date(value).toLocaleString();
    },
    getComment() {
      http
          .get("/comment/" + this.id)
          .then(response => {
            this.comment = response.data;
          })
          .catch(e => {
            console.log(e);
            this.comment = null;
            this.errorMessage = "Комментарий не найден";
          });
    },
    updateComment() {
      const data = {
        content: this.comment.content
      };
      http
          .post("/updateComment/" + this.comment.id, data)
          .then(() => {
            this.$router.push("/task/" + this.comment.taskId);
          })
          .catch(e => {
            console.log(e);
            this.errorMessage = "Не удалось обновить комментарий";
          });
    },
    deleteComment() {
      http
          .post("/deleteComment/" + this.comment.id)
          .then(() => {
            this.$router.push("/task/" + this.comment.taskId);
          })
          .catch(e => {
            console.log(e);
            this.errorMessage = "Не удалось удалить комментарий";
          });
    }
  },
  mounted() {
    this.getComment();
  }
};
</script>
