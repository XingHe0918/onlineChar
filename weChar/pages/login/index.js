//index.js
//获取应用实例

Page({
  data:{
    formData:{
      username:"123",
      password:"123"
    },
    isPassword: true,
  },
  showPassword(){
    this.setData({
      isPassword: !this.data.isPassword
    })
  }
  
})
