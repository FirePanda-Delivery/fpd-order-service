CREATE VIEW average_delivery_time AS
SELECT restaurant_id, city, EXTRACT(MINUTES FROM AVG(time_end - time_start)) as delivery_time FROM "order"
WHERE date > CURRENT_DATE - 30
GROUP BY restaurant_id, city
