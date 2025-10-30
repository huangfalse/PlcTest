import { defineStore } from 'pinia'
export  default defineStore("productGlassType",{
    state: () =>{
        let GlassType=$ref(['',''])
        return {
            GlassType
        }
    }
})