# Fake Tinkoff Payment API

Фейковая реализация платёжного API Тинькофф для тестирования интеграций.

## Стек

- Java 21
- Spring Boot 3.5.14
- PostgreSQL 17
- Maven
- Spring JDBC (JdbcTemplate)

## Установка и запуск

### 1. Клонировать репозиторий
git clone https://github.com/izakuMeow/tinkoff-fake-payment-api.git
cd tinkoff-fake-payment-api

### 2. Создать базу данных
psql -U postgres
CREATE DATABASE tinkoff;
\q

### 3. Создать таблицы
psql -U postgres -d tinkoff

CREATE TABLE IF NOT EXISTS payments (
payment_id       VARCHAR(50)  PRIMARY KEY,
order_id         VARCHAR(100) NOT NULL,
terminal_key     VARCHAR(100) NOT NULL,
amount           BIGINT       NOT NULL,
status           VARCHAR(50)  NOT NULL,
notification_url VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS terminals (
terminal_key VARCHAR(20)  PRIMARY KEY,
name         VARCHAR(100) NOT NULL
);

INSERT INTO terminals (terminal_key, name)
VALUES ('TinkoffBankTest', 'Тестовый терминал');

### 4. Настроить подключение к БД
Скопировать `src/main/resources/application-local.properties.example`
и переименовать в `application-local.properties`.
Вписать свой пароль от PostgreSQL.

### 5. Запустить
./mvnw spring-boot:run

Сервер запустится на порту **8080**.

## Доступные методы

| Метод | URL                 | Описание              |
|-------|---------------------|-----------------------|
| POST  | /v2/Init            | Создать платёж        |
| POST  | /v2/FinishAuthorize | Подтвердить карту     |
| POST  | /v2/Confirm         | Списать деньги        |
| POST  | /v2/Cancel          | Отменить платёж       |
| POST  | /v2/GetState        | Список платежей заказа|

## Жизненный цикл платежа

Init → NEW → FinishAuthorize → AUTHORIZED → Confirm → CONFIRMED
→ Cancel  → CANCELED

## Примеры запросов

### Создать платёж
POST http://localhost:8080/v2/Init
Content-Type: application/json

{
"TerminalKey": "TinkoffBankTest",
"Amount": 150000,
"OrderId": "ORDER-001",
"NotificationURL": "https://your-site.com/webhook"
}

Ответ:
{
"Success": true,
"ErrorCode": "0",
"PaymentId": "149309876",
"Status": "NEW",
"Amount": 150000
}

### Неизвестный терминал
{
"Success": false,
"ErrorCode": "1",
"Message": "Неизвестный терминал"
}

### Получить статус заказа
POST http://localhost:8080/v2/GetState
Content-Type: application/json

{
"OrderId": "ORDER-001"
}

Ответ:
{
"Success": true,
"ErrorCode": "0",
"Payments": [
{ "PaymentId": "149309876", "Status": "CONFIRMED", "Amount": 150000 }
]
}