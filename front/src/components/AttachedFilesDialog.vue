<template>
  <el-dialog
      v-model="dialogVisible"
      title="Files"
      width="70%"
  >
    <el-table :data="fileTableData" style="width: 100%" max-height="300">
      <el-table-column prop="name" label="Name"/>
      <el-table-column prop="author" label="Uploaded by"/>
      <el-table-column prop="date" label="Upload date"/>
      <el-table-column prop="fileType" label="File type"/>
      <el-table-column fixed="right" label="Operations" width="120">
        <template #default="scope">
          <el-button
              type="text"
              size="small"
          >
            Remove
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-upload
        class="upload-box"
        drag
        action="https://jsonplaceholder.typicode.com/posts/"
        multiple
    >
      <span class="material-icons-round upload-icon">cloud_upload</span>
      <div class="el-upload__text">
        Drop file here or <em>click to upload</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          jpg/png files with a size less than 500kb
        </div>
      </template>
    </el-upload>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">Close</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>

import { ref } from "vue"

const fileTableData = ref<{
  name: string,
  author: string,
  date: string,
  fileType: string,
}[]>([])
for (let i = 0; i < 15; i++) {
  fileTableData.value.push({
    name: `name ${i}`,
    author: `author ${i}`,
    date: (new Date()).toDateString(),
    fileType: 'type 0',
  })
}
const dialogVisible = ref(false)
const toggleVisible = () => {
  dialogVisible.value = !dialogVisible.value
}
defineExpose({
  toggleVisible
});
</script>

<style scoped>

.upload-icon {
  margin-top: 40px;
  font-size: 5em;
  color: rgba(0, 0, 0, 0.3);
}

.upload-box {
  margin-top: 16px;
  text-align: center;
}
</style>
