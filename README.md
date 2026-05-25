# Fake Tinkoff Payment API

Фейковая реализация платёжного API Тинькофф для тестирования интеграций.

## Требования

- Java 21
- Maven
- PostgreSQL 17

## Установка и запуск

### 1. Клонировать репозиторий
git clone https://github.com/izakuMeow/tinkoff-fake-payment-api.git
cd tinkoff-fake-payment-api

### 2. Создать базу данных
psql -U postgres
CREATE DATABASE tinkoff;
\q

### 3. Создать файл с настройками
Скопировать `src/main/resources/application-local.properties.example`
и переименовать в `application-local.properties`.
Вписать свой пароль от PostgreSQL.

### 4. Запустить
./mvnw spring-boot:run

Сервер запустится на порту **8080**.

## Доступные методы

| Метод | URL | Описание |
|-------|-----|----------|
| POST | /v2/Init | Создать платёж |
| POST | /v2/FinishAuthorize | Подтвердить карту |
| POST | /v2/Confirm | Списать деньги |
| POST | /v2/Cancel | Отменить платёж |
| POST | /v2/GetState | Проверить статус |

## Пример запроса

### Создать платёж
```json
POST http://localhost:8080/v2/Init
Content-Type: application/json

{
  "TerminalKey": "TinkoffBankTest",
  "Amount": 150000,
  "OrderId": "21090",
  "NotificationURL": "https://your-site.com/webhook"
}
```

### Ответ
```json
{
  "Success": true,
  "ErrorCode": "0",
  "PaymentId": "AB12CD34",
  "Status": "NEW",
  "Amount": 150000
}
```

## Жизненный цикл платежа
Init → NEW
FinishAuthorize → AUTHORIZED
Confirm → CONFIRMED
Cancel → CANCELED

## Webhook

При изменении статуса платежа сервер автоматически отправляет
POST запрос на `NotificationURL`:

```json
{
  "PaymentId": "AB12CD34",
  "Status": "CONFIRMED",
  "Amount": 150000,
  "OrderId": "21090"
}
```