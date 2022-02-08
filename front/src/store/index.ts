import { createStore } from "vuex";

const store = createStore({
  state: {
    windowWidth: {
      type: 'lg',
      width: 0
    }
  },
  mutations: {
    windowResize(state, newWidth: number) {
      state.windowWidth.width = newWidth;
      if (newWidth < 768) {
        state.windowWidth.type = 'xs'
      } else if (newWidth < 1024) {
        state.windowWidth.type = 'md'
      } else {
        state.windowWidth.type = 'lg'
      }
    }
  },
  getters: {
    windowWidth(state) {
      return state.windowWidth
    }
  },
  actions: {}
});

export default store;
