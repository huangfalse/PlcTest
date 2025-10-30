import {defineStore} from 'pinia'
import {ref} from "vue";
export  default defineStore("userInfo",{
    state: () =>{
        let user=ref(null)
        return {
            user
        }
    },
    persist: true//数据持久化，防止刷新数据消失
})