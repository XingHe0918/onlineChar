//index.js
//获取应用实例
const app = getApp()

Page({
  data:{
    username:"xinghe",
    password:"123",
    isPassword: true,
  },

  //显示密码
  showPassword(){
    this.setData({
      isPassword: !this.data.isPassword
    })
  },

  //用户登录
  userRegister(){
    wx.request({
      url: app.getMYURL() + "/user/userRegister",
      method: 'POST',
      data:{
        username: this.data.username,
        password: this.data.password
      },
      header:{
        'content-type' : 'application/json'
      },
      success(res){
        app.setRefreshToken(res.data.RefreshToken);
        app.setAccessToken(res.data.AccessToken);
        if(res.statusCode == 500 || 400){
          wx.showToast({
            title: res.data,
            icon: 'none',
            duration: 2000//持续的时间
          })
        }
      }
    })
  },

  //跳转到注册页面
  toRegisterPage(){
    console.log("跳转");
    wx.navigateTo({
      url: '/pages/register/index',
    })
  }
  
})
