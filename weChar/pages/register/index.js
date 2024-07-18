
const app = getApp()

Page({
  data: {
    imagePath: '../../resource/image/上传头像.png',
    username: null,
    nickename: null,
    gender: null,
    phoneNumber: null,
    email: null,
    address: null,
    verificationCode : null,
    isOk : false
  },
  
  //协议阅读
  changeIsOk(){
    this.data.isOk = ! this.data.isOk
  },

  //获取验证码
  getVerificationCode(){
    if(!this.data.isOk){
      wx.showToast({
        title: "请同意相关协议",
        icon: 'none',
        duration: 2000//持续的时间
      })
      return;
    }
    wx.request({
      url: app.getMYURL() + "/user/getVerificationCode",
      method: 'POST',
      data:{
        email: this.data.email
      },
      header:{
        'content-type' : 'application/json',
        'Authorization': `Bearer ` + app.getAccessToken() // 添加token
      },
      success(res){
        console.log(res);
      },
      fail(err){
        console.log(err);
      }
    })
  }
  
});
