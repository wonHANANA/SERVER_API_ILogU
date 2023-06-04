insert into ILogU.user (created_at, updated_at, email, nickname, password, user_role) values
(NOW(), NOW(), '1@naver.com', '이동현', '1234', 'USER_ROLE'),
(NOW(), NOW(), '2@naver.com', '이건영', '1234', 'USER_ROLE'),
(NOW(), NOW(), '3@naver.com', '이인', '1234', 'USER_ROLE'),
(NOW(), NOW(), '4@naver.com', '함승현', '1234', 'USER_ROLE'),
(NOW(), NOW(), '5@naver.com', '전준휘', '1234', 'USER_ROLE'),
(NOW(), NOW(), '6@naver.com', '김태우', '1234', 'USER_ROLE'),
(NOW(), NOW(), '7@naver.com', '김준기', '1234', 'USER_ROLE'),
(NOW(), NOW(), '8@naver.com', '김보인', '1234', 'USER_ROLE');

insert into ILogU.board (created_at, updated_at, user_id, title, content, category) values
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

insert into ILogU.comment (created_at, updated_at, parent_comment_id, board_id,  user_id, comment) values
(NOW(), NOW(), null, 1, 1, '1빠'),
(NOW(), NOW(), 1, 1, 2, '1빠에 1빠'),
(NOW(), NOW(), 2, 1, 3, '1빠에 1빠에 1빠'),
(NOW(), NOW(), 3, 1, 4, '1빠에 1빠에 1빠에 1빠'),
(NOW(), NOW(), null, 1, 5, '2빠'),
(NOW(), NOW(), 5, 1, 6, '2빠에 1빠'),
(NOW(), NOW(), 5, 1, 7, '2빠에 2빠'),
(NOW(), NOW(), null, 1, 8, '3빠'),

(NOW(), NOW(), null, 2, 1, '4빠'),
(NOW(), NOW(), null, 2, 1, '5빠'),
(NOW(), NOW(), null, 2, 1, '6빠'),
(NOW(), NOW(), null, 2, 1, '7빠'),

(NOW(), NOW(), null, 3, 1, '8빠'),
(NOW(), NOW(), null, 3, 1, '3빠'),
(NOW(), NOW(), null, 3, 1, '3빠'),
(NOW(), NOW(), null, 3, 1, '3빠'),
(NOW(), NOW(), null, 3, 1, '3빠'),
(NOW(), NOW(), null, 3, 1, '3빠');

insert into ILogU.board_image (board_id, created_at, updated_at, original_file_name, upload_file_name, upload_file_path, upload_file_url) values
(1, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '1번'),
(1, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '2번'),
(1, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '3번'),
(1, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '4번'),
(1, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '5번'),
(2, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '6번'),
(2, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '7번'),
(3, NOW(), NOW(), 'weoifj', 'woeifjwoeijf', 'woeifjwojie', '8번');




