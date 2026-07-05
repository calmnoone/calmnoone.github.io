-- Seed data: admin user and products

INSERT IGNORE INTO users (id, username, email, password, role, status) VALUES
(1, 'admin', 'admin@trendshop.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin', 1);

INSERT IGNORE INTO user_profiles (user_id, nickname) VALUES
(1, '超级管理员');

INSERT IGNORE INTO products (id, name, category, price, original_price, image, badge, badge_type, rating, reviews, stock, description) VALUES
(1, '经典纯棉T恤 男女同款', '男装', 129, 259, '/images/products/tshirt.svg', '热卖', 'hot', 4.8, 2300, 200, '舒适透气100%纯棉面料，简约圆领设计'),
(2, '时尚斜挎包 简约百搭', '包包', 199, 399, '/images/products/bag.svg', '新品', 'new', 4.9, 856, 80, '头层牛皮制作，大容量设计，通勤出游皆宜'),
(3, '无线降噪耳机 Pro', '数码', 599, 899, '/images/products/headphone.svg', '', '', 4.7, 1500, 50, '主动降噪，40小时续航，Hi-Res音质认证'),
(4, '简约石英腕表 男款', '配饰', 899, 1599, '/images/products/watch.svg', '限时特惠', 'hot', 4.9, 3100, 30, '进口机芯，蓝宝石镜面，50米防水'),
(5, '复古太阳镜 防紫外线', '配饰', 159, 299, '/images/products/sunglass.svg', '', '', 4.6, 620, 150, '偏光镜片，UV400防护，轻盈钛合金框架'),
(6, '磁吸手机壳 防摔保护', '数码', 79, 129, '/images/products/phonecase.svg', '新品', 'new', 4.7, 428, 300, 'MagSafe磁吸，军工级防摔，亲肤手感涂层'),
(7, '复古运动鞋 老爹鞋', '鞋履', 459, 699, '/images/products/shoe.svg', '爆款', 'hot', 4.8, 4200, 60, 'EVA缓震中底，网面透气鞋面，复古拼色设计'),
(8, '微单相机包 防水便携', '包包', 249, 399, '/images/products/camera.svg', '', '', 4.5, 198, 45, '防水面料，可调节隔层，一机两镜容量'),
(9, '休闲束脚运动裤 男款', '男装', 179, 349, '/images/products/pants.svg', '热卖', 'hot', 4.7, 1800, 120, '弹力棉质面料，束脚版型，运动休闲两穿'),
(10, '法式碎花连衣裙', '女装', 239, 459, '/images/products/dress.svg', '新品', 'new', 4.8, 950, 70, '雪纺面料，收腰A字版型，浪漫碎花印花'),
(11, '真皮手提托特包', '女装', 359, 699, '/images/products/totebag.svg', '热卖', 'hot', 4.9, 2100, 40, '二层牛皮，极简设计，可放14寸笔记本'),
(12, '潮流鸭舌帽 男女同款', '配饰', 89, 159, '/images/products/cap.svg', '', '', 4.5, 720, 200, '纯棉斜纹布，可调节头围，刺绣logo');
