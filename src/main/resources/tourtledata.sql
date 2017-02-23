INSERT INTO tour VALUES(DEFAULT, 'Island Breakfast')
INSERT INTO tour VALUES(DEFAULT, 'Afternoon Delight')
INSERT INTO tour VALUES(DEFAULT, 'The Sweetness')
INSERT INTO tour VALUES(DEFAULT, 'Feed Me')
INSERT INTO tour VALUES(DEFAULT, 'Show Me the World');

INSERT INTO poi VALUES
  (0, 'JJ Bean', 1.0, 1.0, 'Fresh-roasted coffee from sustainable growers around the world.', 'example.jpg'),
  (1, 'Terra Breads', 1.0, 2.0, 'Artisan breads and pastries', 'example.jpg'),
  (2, 'Oyama Sausage Co.', 2.0, 1.0, 'Gourmet meats and sausages.  Traditional delicacies and modern gourmet creations, crafted by master German sausage makers and using fresh local ingredients. ',
   'example.jpg'),
  (3, 'Our Little Flower Company', 2.0, 2.0, 'Flowers to brighten any occasion!', 'example.jpg'),
  (4, 'No. 1 Orchard', 2.0, 3.0, 'Local fresh fruit - a must for fruit lovers!',
                        'example.jpg'),
  (5, 'Benton Brothers Fine Cheese', 3.0, 2.0, 'Select hand-crafted fine cheeses',
                        'example.jpg'),
  (6, 'Lee''s Donuts', 3.0, 3.0, 'Hand-crafting donuts on Granville Island for over 30 years!  Come watch the donutiers throughout the day!',
   'example.jpg'),
  (7, 'Stuart''s Bakery', 3.0, 4.0, 'Fresh pastries, desserts, and breads cooked in a stone deck oven',
   'example.jpg'),
  (8, 'A la Mode', 4.0, 3.0, 'Homemade sweet and savory pies for every occasion', 'example.jpg'),
  (9, 'South China Seas Trading Company', 4.0, 4.0, 'The finest and freshest herbs and spices from all over the world!',
   'example.jpg'),
  (10, 'The Stock Market', 4.0, 5.0, 'Fresh soups, sauces, and spreads, made daily', 'example.jpg'),
  (11, 'Granville Island Tea Company', 5.0, 4.0, 'Teas from around the world, explained by local experts',
   'example.jpg'),
  (12, 'Bon Macaron Patisserie', 5.0, 5.0, 'Ever-changing selection of maracons, using flavors from around Granville Island',
   'example.jpg'),
  (13, 'Vancouver Foodie Tours', 5.0, 6.0, 'Vancouver''s Number 1 Tour on TripAdvisor', 'example.jpg');

INSERT INTO tours_pois VALUES
  (2, 0), (2, 1), (2, 4), (2, 6), (2, 7),
  (12, 6), (12, 7), (12, 8), (12, 12),
  (22, 1), (22, 2), (22, 6), (22, 8), (22, 10), (22, 13),
  (32, 2), (32, 3), (32, 5), (32, 9), (32, 11), (32, 13),
  (42, 2), (42, 3), (42, 4), (42, 5), (42, 8), (42, 11), (42, 12);


