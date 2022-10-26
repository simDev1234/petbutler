/* 비밀번호 규칙 */
const reg = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}/;

/* 집사 경력 숫자 여부 */
const reg_num = /[0-9]{1,2}/;

/* 비밀번호 유효성 체크 */
function passwordCheck(){
  let password = $("#password").val();
  console.log(password);
  let rePassword = $("#rePassword").val();

  if (password == null) {
    alert("비밀번호를 입력해주세요.")
    $("#password").focus();
    return false;
  }

  if (rePassword == null) {
    alert("비밀번호를 재입력해주세요.")
    $("#rePassword").focus();
    return false;
  }

  if (!reg.test(password)) {
    alert("비밀번호는 8자리 이상으로 영문자, 숫자, 특수문자 포함 문자로 작성해주세요.")
    $("#password").val("");
    $("#password").focus();
    return false;
  }

  if (password != rePassword) {
    alert("비밀번호가 일치하지 않습니다.")
    $("#rePassword").val("");
    $("#rePassword").focus();
    return false;
  }

  return true;
}

/* 집사 경력 유효성 체크 */
function butlerLevelCheck(){
  let butlerLevel =  $("#butlerLevel").val();

  if (butlerLevel == null) {
    alert('집사 경력을 작성해주세요.');
    return false;
  }

  if (!reg_num.test(butlerLevel)) {
    alert('집사 경력은 숫자 일의자리수 또는 십의자리수로 작성해주세요.');
    return false;
  }
}

/* 회원가입 */
function register(f){

  if (passwordCheck() == false) {
    return false;
  }

  if (butlerLevelCheck() == false) {
    return false;
  }

  f.submit();
  return true;
}

/* 집사 경력 셀렉박스 */
$(function(){
  $("select").on('change',function(){

    $("#butlerLevel").attr('placeholder',this.value);

    if (this.value == '직접입력') {
      $("#butlerLevel").focus();
    } else {
      $("#butlerLevel").attr('readonly',false);
      console.log(this.value);
      $("#butlerLevel").val(this.value);
    }

  });
});