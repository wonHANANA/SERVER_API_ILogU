package com.onehana.server_ilogu.dto.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 100번대 에러 : 상태가 괜찮음
     */


    /**
     * 200번대 에러 : 요청이 성공적임
     */
    SUCCESS(true, "200-00-01", "요청에 성공하였습니다."),

    /**
     * 300번대 에러 : 요청에 대해 하나 이상의 응답이 가능함.
     */

    /**
     * 400번대 에러 : 클라이언트의 잘못된 요청으로 인한 에러들
     */

    TEST_ENDPOINT_EMPTY(false, "400-02-01", "end-point를 입력해주세요 예) end-point: one-hana.site:8080."),
    TEST_BINDING_EMPTY(false, "400-02-02","binding을 입력해주세요 예) binding: [회원가입] 회원가입 입력 에러 테스트."),
    TEST_TESTCODES_EMPTY(false, "400-02-03", "testCodes를 입력해주세요 예) testCodes: [회원가입] 회원가입 입력 에러 테스트."),
    TEST_TESTCODE_IS_NOT_OBJECT(false, "400-02-04", "testCodes는 object 형식으로 입력해주세요."),
    TEST_INNERTEST_NAME_EMPTY(false, "400-02-05", "testCodes 안에 있는 test를 입력해주세요. 예) \"test\": \"이메일 형식 확인\""),
    TEST_INNERTEST_ROUTE_EMPTY(false, "400-02-06", "testCodes 안에 있는 route를 입력해주세요. 예) \"route\": \"/user/signin\""),
    TEST_INNERTEST_EXPECT_EMPTY(false, "400-02-07", "testCodes 안에 있는 expect를 입력해주세요. 예) \"expect\": {}"),
    TEST_INNERTEST_WHERE_EMPTY(false, "400-02-08", "testCodes 안에 있는 expect 안에 있는 where를 입력해주세요. 예) \"where\": \"response.body.code\""),
    TEST_INNERTEST_TOBE_EMPTY(false, "400-02-09", "testCodes 안에 있는 expect 안에 있는 toBe를 입력해주세요. 예) \"toBe\": 200"),
    TEST_INNERTEST_METHOD_EMPTY(false, "400-02-10", "testCodes 안에 있는 method를 입력해주세요. 예) \"method\": \"post\""),

    INVALID_HEADER(false, "400-03-01", "Header가 null이거나 형식이 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN(false, "400-03-02", "Access 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(false, "400-03-03", "Refresh 토큰이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(false, "400-03-04", "Access 토큰이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(false, "400-03-05", "Refresh 토큰이 만료되었습니다. 로그인이 필요합니다."),

    DUPLICATED_USERID(false, "400-04-01", "이미 가입된 아이디입니다."),
    DUPLICATED_EMAIL(false, "400-04-02", "이미 가입된 이메일입니다."),
    USER_NOT_FOUND(false, "400-04-03", "존재하지 않는 아이디입니다."),
    INVALID_PASSWORD(false, "400-04-04", "비밀번호가 일치하지 않습니다."),
    INVALID_PERMISSION(false, "400-04-05", "권한이 존재하지 않습니다."),

    BOARD_NOT_FOUND(false, "400-05-01", "존재하지 않는 게시물입니다."),
    COMMENT_NOT_FOUND(false, "400-05-02", "존재하지 않는 댓글입니다."),
    IMAGE_NOT_FOUND(false, "400-05-03", "존재하지 않는 사진입니다."),

    EMPTY_STRING(false, "400-06-01", "빈 문자열을 입력하셨습니다."),
    INVALID_VALUE_TYPE(false, "400-06-02", "잘못된 자료형을 입력했습니다."),
    INVALID_JSON_REQUEST(false, "400-06-03", "JSON에 null값이나 잘못된 형식이 포함되어 있습니다."),

    /**
     * 500번대 에러 : 서버 에러 등 서버 프로그래밍 잘못으로 인한 에러들
     */
    DATABASE_CONNECTION_ERROR(false, "500-00-01", "DB관련 에러 발생."),
    PARSE_EXCEPTION_ERROR(false, "500-01-01", "파싱 작업 중 에러 발생"),

    DATABASE_DUPLICATE_VALUE(false, "500-01-02", "DB에 중복된 값이 이미 존재합니다."),

    UNKNOWN_SERVER_ERROR(false, "500-00-00", "아직 처리 되지 않은 서버 오류입니다.");

    private final boolean isSuccess;
    private final String code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, String code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
