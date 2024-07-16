//index.js
//获取应用实例
const app = getApp()

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
  },

  userRegister(){
    wx.request({
      url: app.getMYURL() + "/user/userRegister",
      method: 'POST',
      data:{
        username: this.data.formData.username,
        password: this.data.formData.password
      },
      header:{
        'content-type' : 'application/json'
      },
      success(res){
        console.log(res);
      },
      fail(err) {
        console.error(err);
      }
    })
  }
  
})
