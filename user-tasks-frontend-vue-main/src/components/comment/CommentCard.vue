<template>
  <div class="card mt-4">
    <div class="card-header">Комментарии</div>
    <div class="card-body">
      <div v-if="loading" class="text-muted">Загрузка...</div>
      <div v-else-if="comments.length === 0" class="text-muted">Комментариев пока нет</div>
      <div
          v-for="comment in comments"
          :key="comment.id"
          class="border-bottom mb-3 pb-2"
      >
        <div class="d-flex justify-content-between align-items-start">
          <strong>{{ comment.username }}</strong>
          <small class="text-muted">{{ formatDate(comment.timestamp) }}</small>
        </div>
        <p class="mb-2 mt-1">{{ comment.content }}</p>
        <router-link :to="'/comment/' + comment.id" class="btn btn-sm btn-outline-primary">
          Редактировать
        </router-link>
      </div>

      <form v-if="currentUser" @submit.prevent="addComment" class="mt-3">
        <label class="form-label">Прокомментировать</label>
        <textarea
            v-model="newComment"
            class="form-control"
            rows="3"
            placeholder="Текст комментария"
            required
        />
        <button type="submit" class="btn btn-success mt-2" :disabled="submitting">
          Добавить
        </button>
      </form>
      <p v-else class="text-muted mt-3 mb-0">
        <router-link to="/login">Войдите</router-link>, чтобы оставить комментарий.
      </p>
      <div v-if="errorMessage" class="alert alert-danger mt-2" role="alert">{{ errorMessage }}</div>
    </div>
  </div>
</template>

<script>
import http from "../../http-common";

export default {
  name: "CommentCard",
  props: {
    taskId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      comments: [],
      newComment: "",
      loading: false,
      submitting: false,
      errorMessage: ""
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  methods: {
    formatDate(value) {
      if (!value) {
        return "";
      }
      return new Date(value).toLocaleString();
    },
    loadComments() {
      this.loading = true;
      this.errorMessage = "";
      http
          .get(`/comments/task/${this.taskId}`)
          .then(response => {
            this.comments = response.data;
          })
          .catch(e => {
            console.log(e);
            this.errorMessage = "Не удалось загрузить комментарии";
          })
          .finally(() => {
            this.loading = false;
          });
    },
    addComment() {
      if (!this.currentUser) {
        return;
      }
      this.submitting = true;
      this.errorMessage = "";
      const data = {
        content: this.newComment,
        task_id: Number(this.taskId),
        user_id: this.currentUser.id
      };
      http
          .post("/addComment", data)
          .then(() => {
            this.newComment = "";
            this.loadComments();
          })
          .catch(e => {
            console.log(e);
            this.errorMessage = "Не удалось добавить комментарий";
          })
          .finally(() => {
            this.submitting = false;
          });
    }
  },
  mounted() {
    this.loadComments();
  },
  watch: {
    taskId() {
      this.loadComments();
    }
  }
};
</script>
