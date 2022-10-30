/* 비밀번호 규칙 */
const reg = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}/;

/* 집사 경력 숫자 여부 */
const reg_num = /[0-9]{1,2}/;

/* 비밀번호 유효성 체크 */
function passwordCheck(){
  let password = $("#password").val();
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

/* 반려동물 등록 유효성 체크 */
function petNameExistCheck(){

  let count = $("input.name").filter((name) => name.val() == null || name.val() == "").count();

  if (count > 0) {
    return false;
  }

  return true;
}

/* 회원가입 */
async function register(f) {

  /* password validation */
  if (passwordCheck() == false) {
    return false;
  }

  /* butlerLevel validation */
  if (butlerLevelCheck() == false) {
    return false;
  }

  /* pet name exist validation */
  if (petNameExistCheck() == false) {
    return false;
  }

  /* 회원 정보 */
  const formData = new FormData();

  let email = $('#email').val();
  let password = $('#password').val();
  let butlerLevel = $('#butlerLevel').val();
  let phone = $('#phone').val();

  formData.append("email", email);
  formData.append("password", password);
  formData.append("phone", phone);
  formData.append("butlerLevel", butlerLevel);

  /* 반려 동물 정보 */
  $("input.thumbnail").forEach((thumb) => formData.append("thumbnail", thumb.files[0]));
  $("input.kind").forEach((kind) => formData.append("kind", kind.val()));
  $("input.name").forEach((name) => formData.append("name", name.val()));

  /* http request - post */
  const url = '/users/customer/sign-up';
  const config = {
    headers: {
      "Content-Type": "multipart/form-data"
    },
  };

  await axios.post(url, formData, config).then(function (response) {

    // 정상적일 때
    alert('회원 가입이 정상적으로 완료되었습니다.');
    location.href = '/';

  }).catch(function (err) {
    console.log(err);
  });

}

$(function(){

  /* 집사 경력 셀렉박스 */
  $("select").on('change',function(){

    $("#butlerLevel").attr('placeholder',this.value);

    if (this.value == '직접입력') {
      $("#butlerLevel").attr('readonly',false);
      $("#butlerLevel").focus();
    } else {
      $("#butlerLevel").attr('readonly',true);
      $("#butlerLevel").val(this.value);
    }

  });

  /* 반려 동물 동적 추가 */
  $("#add").on('click', function () {
    let lastField = $("#pets div.fieldwrapper:last");
    let intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
    let fieldWrapper = $("<div class=\"fieldwrapper\" id=\"field" + intId + "\"/>");
    fieldWrapper.data("idx", intId);
    let fThumbnail = $("<p>\n"
        + "          <label>썸네일</label>\n"
        + "          <input type = \"file\" name = \"thumbnail\" class = \"thumbnail\">\n"
        + "        </p>");
    let fKind = $("<p>\n"
        + "          <label>종구분</label>\n"
        + "          <select class = 'kind' name = \"kind\" class = \"kind\">\n"
        + "            <option value = \"dog\">강아지</option>\n"
        + "            <option value = \"cat\">고양이</option>\n"
        + "            <option value = \"etc\">기타</option>\n"
        + "          </select>\n"
        + "        </p>");
    let fName = $("<p>\n"
        + "          <label>이름</label>\n"
        + "          <input type = \"text\" name = \"name\" class = \"name\">\n"
        + "        </p>");
    let removeButton = $("<input type=\"button\" class=\"remove\" value=\"-\" />");
    removeButton.click(function () {
      $(this).parent().remove();
    });
    fieldWrapper.append(fThumbnail);
    fieldWrapper.append(fKind);
    fieldWrapper.append(fName);
    fieldWrapper.append(removeButton);
    $("#pets").append(fieldWrapper);
  });
});