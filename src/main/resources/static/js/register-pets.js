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