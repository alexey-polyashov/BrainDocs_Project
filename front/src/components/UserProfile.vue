<template>
  <el-card class="log-form-box">
    <template #header>
      <div class="card-header">
        <span style="font-size: xx-large">Profile</span>
      </div>
    </template>
    <el-descriptions
        title="User Info"
        border
        :column="1"
    >
      <template #extra>
        <el-button type="primary" @click="fieldsEditable = !fieldsEditable">Edit mode {{ fieldsEditable ? 'on' : 'off' }}</el-button>
      </template>
      <el-descriptions-item label="Avatar">
        <el-avatar :size="80" :src="userInfo.avatarSrc">
          <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
        </el-avatar>
      </el-descriptions-item>
      <el-descriptions-item label="Full name">
        <el-input :readonly="!fieldsEditable" v-model="userInfo.fullName" />
      </el-descriptions-item>
      <el-descriptions-item label="Login">
        <el-input :readonly="!fieldsEditable" v-model="userInfo.login" />
      </el-descriptions-item>
      <el-descriptions-item label="Email">
        <el-input :readonly="!fieldsEditable" v-model="userInfo.email" />
      </el-descriptions-item>
      <el-descriptions-item label="Address">
        <el-input :readonly="!fieldsEditable" v-model="userInfo.address" />
      </el-descriptions-item>
      <el-descriptions-item label="Phone">
        <el-input :readonly="!fieldsEditable" v-model="userInfo.phone" />
      </el-descriptions-item>
    </el-descriptions>
    <el-button type="primary" :disabled="!fieldsModified" style="margin-top: 16px">Save changes</el-button>
    <el-button style="margin-top: 16px" @click="passwordChangePrompt">Change password</el-button>
  </el-card>
  <el-dialog
      v-model="passChangeDialogVisible"
      title="Change password"
      width="30%"
  >
    <el-form
        ref="passChangeFormRef"
        :model="passChangeForm"
        label-width="150px"
        :rules="passChangeRules"
    >
      <el-form-item required label="Old password" prop="oldPass">
        <el-input type="password" v-model="passChangeForm.oldPass"></el-input>
      </el-form-item>
      <el-form-item required label="New password" prop="newPass">
        <el-input type="password" v-model="passChangeForm.newPass"></el-input>
      </el-form-item>
      <el-form-item required label="Repeat" prop="newPassRepeated">
        <el-input type="password" v-model="passChangeForm.newPassRepeated"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="passChangeDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="passwordChangeSubmit">Submit</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { ElForm, ElMessage } from "element-plus"

const userInfo = reactive({
  avatarSrc: '',
  fullName: 'Full name',
  login: 'asdf',
  password: 'asdf',
  email: 'asdf@gmail.com',
  address: 'No.1188, Wuzhong Avenue, Wuzhong District, Suzhou, Jiangsu Province',
  phone: '3456457433',
})
const passChangeFormRef = ref<InstanceType<typeof ElForm>>()
const fieldsEditable = ref(false)
const fieldsModified = ref(false)
const passChangeDialogVisible = ref(false)
const passChangeForm = reactive({
  oldPass: '',
  newPass: '',
  newPassRepeated: ''
})
watch(userInfo, () => {
  fieldsModified.value = true
})

const passwordChangePrompt = () => {
  passChangeDialogVisible.value = true
}

const passwordChangeSubmit = () => {
  passChangeFormRef.value?.validate((isValid, invalidFields) => {
    console.log(isValid)
    if (isValid) {
      passChangeDialogVisible.value = false
    } else {
      ElMessage.error('Some inputs are invalid')
    }
  })
}

const passwordMatchValidator = (rule: unknown, value: unknown, callback: (er?: Error) => void) => {
  if (passChangeForm.newPass === passChangeForm.newPassRepeated) {
    callback()
  } else {
    callback(new Error("Two inputs don't match!"))
  }
}

const passChangeRules = reactive({
  newPassRepeated: [
    {
      validator: passwordMatchValidator,
    }
  ]
})
</script>

<style scoped>
</style>
