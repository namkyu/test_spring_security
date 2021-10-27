
-- user (password : 1111)
insert into user(user_name, password, enabled, changed, created) values('admin', '$2a$10$oEgkhV/g3dp/XiQRJtJeFOOuDt2eF84uSvWjG4rpni8VRm96DCCmG', true, NOW(), NOW());
insert into user(user_name, password, enabled, changed, created) values('user', '$2a$10$oEgkhV/g3dp/XiQRJtJeFOOuDt2eF84uSvWjG4rpni8VRm96DCCmG', true, NOW(), NOW());
INSERT INTO user_authority(user_id, authority) VALUES(1, 'ROLE_ADMIN');
INSERT INTO user_authority(user_id, authority) VALUES(2, 'ROLE_USER');