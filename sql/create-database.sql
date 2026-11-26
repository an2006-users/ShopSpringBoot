-- Chạy 1 lần trong SQL Server Management Studio trước khi chạy ứng dụng.
-- Các bảng (categories, users) do Hibernate tự tạo (spring.jpa.hibernate.ddl-auto=update).
IF DB_ID('ShopSpringBoot') IS NULL
    CREATE DATABASE ShopSpringBoot;
GO
