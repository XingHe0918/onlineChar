
const app = getApp()

Page({
  data: {
    imagePath: '../../resource/image/上传头像.png',
    username: null,
    password: null,
    nickname: null,
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
    if(!this.validateEmail(this.data.email))return
    if(!this.data.isOk){
      wx.showToast({
        title: "请同意相关协议",
        icon: 'none',
        duration: 2000//持续的时间
      })
      return;
    }
    wx.request({
      url: app.getMYURL() + "/login-service/getVerificationCode",
      method: 'POST',
      data:{
        email: this.data.email
      },
      header:{
        'content-type' : 'application/json',
        // 'Authorization': `Bearer ` + app.getAccessToken() // 添加token
      },
      success(res){
        console.log(res);
      },
      fail(err){
        console.log(err);
      }
    })
  },

  // 验证用户名
  validateUsername(username) {
    const regex = /^[a-zA-Z0-9_]{5,20}$/;
    if (!regex.test(username)) {
      wx.showToast({ title: '用户名只能包含5-20个字母、数字和下划线', icon: 'none' });
      return false;
    }
    return true;
  },

  // 验证密码
  validatePassword(password) {
    const regex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=]).{8,}$/;
    if (!regex.test(password)) {
      wx.showToast({ title: '密码必须包含至少8个字符，其中一个大写字母、小写字母、数字和特殊字符', icon: 'none' });
      return false;
    }
    return true;
  },

  // 验证昵称
  validateNickname(nickname) {
    if(nickname == null){
      wx.showToast({
        title: '请输入昵称' ,icon: 'none',
      })
      return false;
    }
    if (nickname.length > 30) {
      wx.showToast({ title: '昵称长度不能超过30个字符', icon: 'none' });
      return false;
    }
    return true;
  },

  // 验证性别
  validateGender(gender) {
    const validGenders = ['男', '女', '其他', '不愿透露'];
    if (!validGenders.includes(gender)) {
      wx.showToast({ title: '请选择有效的性别', icon: 'none' });
      return false;
    }
    return true;
  },

  // 验证电话号码
  validatePhoneNumber(phoneNumber) {
    const regex = /^\+?[0-9]*$/;
    if (!regex.test(phoneNumber)) {
      wx.showToast({ title: '电话号码格式不正确', icon: 'none' });
      return false;
    }
    return true;
  },

  // 验证电子邮件
  validateEmail(email) {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!regex.test(email)) {
      wx.showToast({ title: '无效的电子邮件地址', icon: 'none' });
      return false;
    }
    return true;
  },

  // 验证验证码
  validateVerificationCode(verificationCode) {
    const regex = /^\d{6}$/;
    if (!regex.test(verificationCode)) {
      wx.showToast({ title: '验证码格式不正确', icon: 'none' });
      return false;
    }
    return true;
  },

  // 提交表单时的处理函数
  submitForm() {

    if(!this.data.isOk){
      wx.showToast({
        title: "请同意相关协议",
        icon: 'none',
        duration: 2000//持续的时间
      })
      return;
    }

    const { username, password, nickname, gender, phoneNumber, email, verificationCode } = this.data;
    if (
      this.validateUsername(username) &&
      this.validatePassword(password) &&
      this.validateNickname(nickname) &&
      this.validateGender(gender) &&
      this.validatePhoneNumber(phoneNumber) &&
      this.validateEmail(email) &&
      this.validateVerificationCode(verificationCode)
    ) {
      // 提交表单
      wx.request({
        url: app.getMYURL() + "/login-service/addUser",
        method: 'POST',
        data:{
          username: this.data.username,
          nickname: this.data.nickname,
          gender: this.data.gender,
          phoneNumber: this.data.phoneNumber,
          email: this.data.email,
          address: this.data.address,
          password: this.data.password,
          verificationCode: this.data.verificationCode,
          headImage: this.data.imagePath
        },
        header:{
          'content-type' : 'application/json',
          // 'Authorization': `Bearer ` + app.getAccessToken() // 添加token
        },
        success(res){
          if(res.statusCode === 200){
            
            wx.showToast({
              title: '注册成功',
              icon: 'success',
              duration: 2000
            })
            return;
          }
          wx.showToast({
            title: res.data,
            icon: 'none',
            duration: 2000//持续的时间
          })
        },
        fail(err){
          console.log(err);
        }
      })
    }
  },

  // 更新表单字段
  updateField(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({
      [field]: e.detail.value
    });
  },

  //头像更换
  image_click(){
    var that = this
    wx.chooseImage({
      count: 1, // 可以选择一张图片
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success (res) {
        const tempFilePaths = res.tempFilePaths;
        // 处理选择的图片路径，如上传到服务器或显示在页面上
        that.setData({
          imagePath: tempFilePaths[0] // 假设你只选择了一张图片，取第一个
        });
      },
      fail (err) {
        // 处理失败的情况
        console.error(err);
      }
    });    
}

  
});
