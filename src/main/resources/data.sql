insert into ILogU.user (created_at, updated_at, email, nickname, password, user_role) values
(NOW(), NOW(), '1@naver.com', '이동현', '1234', 'USER_ROLE'),
(NOW(), NOW(), '2@naver.com', '이건영', '1234', 'USER_ROLE'),
(NOW(), NOW(), '3@naver.com', '이인', '1234', 'USER_ROLE'),
(NOW(), NOW(), '4@naver.com', '함승현', '1234', 'USER_ROLE'),
(NOW(), NOW(), '5@naver.com', '전준휘', '1234', 'USER_ROLE'),
(NOW(), NOW(), '6@naver.com', '김태우', '1234', 'USER_ROLE'),
(NOW(), NOW(), '7@naver.com', '김준기', '1234', 'USER_ROLE'),
(NOW(), NOW(), '8@naver.com', '김보인', '1234', 'USER_ROLE');

insert into ILogU.board (created_at, updated_at, user_id, content, title, category) values
 (NOW(), NOW(), 1, '제목이다.', '글을 쓴다', 'COOK'),
 (NOW(), NOW(), 1, '제목이다.', '밥을 먹다', 'COOK'),
 (NOW(), NOW(), 1, '제목이다.', '뭐든 한다', 'FINANCE'),
 (NOW(), NOW(), 2, '제목이다.', '제육덮밥', 'COOK'),
 (NOW(), NOW(), 3, '제목이다.', '그래도', 'TRAVEL'),
 (NOW(), NOW(), 3, '제목이다.', '맛있다', 'EXERCISE'),
 (NOW(), NOW(), 4, '제목이다.', '우동', 'FINANCE'),
 (NOW(), NOW(), 4, '제목이다.', '모밀', 'TRAVEL'),
 (NOW(), NOW(), 4, '제목이다.', '정식', 'EXERCISE'),
 (NOW(), NOW(), 4, '제목이다.', '든든', 'FINANCE'),
 (NOW(), NOW(), 4, '제목이다.', '하다', 'EXERCISE'),
 (NOW(), NOW(), 5, '제목이다.', '배불러', 'TRAVEL'),
 (NOW(), NOW(), 5, '제목이다.', '자고싶다', 'FINANCE'),
 (NOW(), NOW(), 6, '제목이다.', '사실 구라임', 'FINANCE'),
 (NOW(), NOW(), 6, '제목이다.', '대댓글 해야돼', 'EXERCISE'),
 (NOW(), NOW(), 6, '제목이다.', 'ㅇㅋㅇㅋ', 'FINANCE'),
 (NOW(), NOW(), 6, '제목이다.', 'ㅇㅇ', 'EXERCISE'),
 (NOW(), NOW(), 7, '제목이다.', '할말 없음', 'TRAVEL'),
 (NOW(), NOW(), 8, '제목이다.', '그래', 'TRAVEL'),
 (NOW(), NOW(), 8, '제목이다.', '많이', 'TRAVEL'),
 (NOW(), NOW(), 8, '제목이다.', '써보자', 'TRAVEL'),
 (NOW(), NOW(), 8, '제목이다.', '그래그래', 'TRAVEL'),
 (NOW(), NOW(), 8, '제목이다.', '좋다', 'TRAVEL');



