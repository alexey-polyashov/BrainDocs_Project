<template>
  <el-card class="log-form-box">
    <template #header>
      <div class="card-header">
        <span>Add new document</span>
      </div>
    </template>
    <el-form ref="formRef" :model="form" label-width="auto">
      <el-form-item label="Id">
        <el-input type="number" v-model="form.id"></el-input>
      </el-form-item>
      <el-form-item label="Document type">
        <el-input v-model="form.docType"></el-input>
      </el-form-item>
      <el-form-item label="Document name">
        <el-input v-model="form.docName"></el-input>
      </el-form-item>
      <el-form-item label="Date">
        <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="Pick a date"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="Organization">
        <el-input v-model="form.organization"></el-input>
      </el-form-item>
      <h2 style="text-align: center">Content</h2>
      <editor
          api-key='r8zqd3nkvkfr7t0s0r4qo107guk38q9xpact4xap39t1p0pe'
          />
      <AttachedFilesDialog ref="filesDialog" />
      <el-badge :value="15" style="margin-top: 16px">
        <el-button size="large" @click="toggleFileAttachDialog">Attach files</el-button>
      </el-badge>
    </el-form>
  </el-card>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue"
import Editor from '@tinymce/tinymce-vue'
import AttachedFilesDialog from "./AttachedFilesDialog.vue"

const filesDialog = ref()
const form = reactive({
  docType: '',
  id: '',
  organization: '',
  date: '',
  docName: '',
})

const toggleFileAttachDialog = () => {
  filesDialog.value?.toggleVisible()
};
</script>

<style scoped>
.log-form-box {
  margin-top: 0;
}

.card-header {
  font-size: x-large;
  font-weight: bold;
}
</style>
