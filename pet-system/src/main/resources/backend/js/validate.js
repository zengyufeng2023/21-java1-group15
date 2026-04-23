
function isValidUsername (str) {
  return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone (val) {
  if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
    return false
  } else {
    return true
  }
}

//校验账号
function checkUserName (rule, value, callback){
  if (value == "") {
    callback(new Error("请输入账号"))
  } else if (value.length > 20 || value.length <3) {
    callback(new Error("账号长度应是3-20"))
  } else {
    callback()
  }
}

//校验姓名
function checkName (rule, value, callback){
  if (value == "") {
    callback(new Error("请输入姓名"))
  } else if (value.length > 12) {
    callback(new Error("账号长度应是1-12"))
  } else {
    callback()
  }
}

function checkPhone (rule, value, callback){
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value == "") {
    callback(new Error("请输入手机号"))
  } else if (!isCellPhone(value)) {//引入methods中封装的检查手机格式的方法
    callback(new Error("请输入正确的手机号!"))
  } else {
    callback()
  }
}


function validID (rule,value,callback) {

  let reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
  if(value == '') {
    callback(new Error('请输入邮箱'))
  } else if (reg.test(value)) {
    callback()
  } else {
    callback(new Error('邮箱不正确'))
  }
}