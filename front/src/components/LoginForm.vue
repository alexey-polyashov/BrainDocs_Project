<template>
  <el-form ref="formRef" class="log-form-box" :model="form" label-width="auto">
    <el-form-item label="Login" prop="login">
      <el-input v-model="form.login"></el-input>
    </el-form-item>
    <el-form-item label="Password" prop="password">
      <el-input type="password" v-model="form.password"></el-input>
    </el-form-item>
    <el-form-item>
      <transition name="el-fade-in-linear" mode="out-in">
        <el-button v-if="!loading" type="primary" @click="onSubmit">Login</el-button>
        <el-button v-else type="primary" loading>Login</el-button>
      </transition>
      <el-button @click="resetForm(formRef)">Reset</el-button>
    </el-form-item>
    <el-button @click="switchToRegister" type="text">Don't have an account yet? Register instead</el-button>
  </el-form>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { ElForm } from "element-plus";
import { useRouter } from "vue-router";

type FormInstance = InstanceType<typeof ElForm>;
const router = useRouter();
const loading = ref(false);
const form = reactive({
  login: '',
  password: ''
});
const formRef = ref<FormInstance>();
const onSubmit = () => {
  loading.value = true;
  setTimeout(() => loading.value = false, 1000);
};
const resetForm = (formEl: FormInstance | undefined) => {
  formEl?.resetFields();
}
const switchToRegister = () => {
  router.push({ name: 'register' });
}
</script>
