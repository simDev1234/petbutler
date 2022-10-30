/* 비밀번호 규칙 */
const reg = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}/;

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

/* 회원가입 */
function register(f){

  if (passwordCheck() == false) {
    return false;
  }

  f.submit();
  return true;
}

/* 우편 주소 */
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ''; // 주소 변수

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else { // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById('postcode').value = data.zonecode;
      document.getElementById("address").value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("detailAddress").focus();
    }
  }).open();
}