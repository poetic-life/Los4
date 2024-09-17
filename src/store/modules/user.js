import { getInfo, setInfo, removeInfo } from '@/utils/storage';

const state = {
  userInfo: getInfo()
};

const mutations = {
  setUserInfo(state, obj) {
    state.userInfo = obj;
    setInfo(obj);
  },
  removeInfo(){
    removeInfo()
  }
};

const actions = {
  logout (context) {
    // 个人信息要重置
    context.commit('setUserInfo', {})
  }
};

const getters = {
  token(state) {
    return state.userInfo.token;
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
