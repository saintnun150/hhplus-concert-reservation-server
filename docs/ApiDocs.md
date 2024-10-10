# API 명세서

## 목차

1. [인증 정보](#1-인증-정보)
2. [API](#2-api)
  - [대기열](#대기열)
    1. [대기열 토큰 발급](#1-대기열-토큰-발급)
    2. [대기열 토큰 상태 조회](#2-대기열-토큰-상태-조회)
  - [사용자](#사용자)
    1. [계좌 정보 조회](#1-계좌-정보-조회)
    2. [계좌 잔액 충전](#2-계좌-잔액-충전)
  - [콘서트](#콘서트)
    1. [콘서트 목록 조회](#1-콘서트-목록-조회-api)
    2. [예약 가능한 콘서트 날짜 조회](#2-예약-가능한-콘서트-날짜-조회-api)
    3. [예약 가능한 좌석 조회](#3-예약-가능한-좌석-조회-api)
    4. [좌석 예약](#4-좌석-예약-api)
  - [결제](#결제)
    1. [결제 요청](#결제-요청-api)

---

## 1. 인증 정보
`대기열 토큰 발급 API` 와 `사용자 잔액 조회, 충전 API`를 제외한, <br/>
모든 API는  X-QUEUE-TOKEN 헤더에 대기열 토큰을 포함하여 검증한다.

- 헤더 이름: X-QUEUE-TOKEN
- 위치: 헤더
  <br/>

## 2. API
- **공통 path**: `/api/v1`


### 대기열


### 1. 대기열 토큰 발급
- **설명**: 사용자 ID를 통해 대기열 토큰을 반환하거나 생성후 토큰 정보를 반환
- **HTTP 메소드**: `POST`
- **URL**: `/waiting-queues/tokens`

---

**Request**

- **헤더**
    - ` X-QUEUE-TOKEN: ${token}`

- **요청 본문** (`POST`)

  ```json
  {
    "userId": "value"
  }
    ```

**Response**
- 성공 (http status code: 200)
```json
{
  "statusCode": 200,
  "data": {
    "tokenId": 5,
    "token": "uuid-token1",
    "waitingQueueStatus": "WAITING"
  }
}
```
- 실패 (http status code: 404)
```json
{
  "code": "user_err_1",
  "message": "user not found"
}
```

### 2. 대기열 토큰 상태 조회
- **설명**: token으로 조회하여 현재 대기열 상태 정보 반환
- **HTTP 메소드**: `GET`
- **URL**: `/tokens`

**Request**

- **헤더**
    - ` X-QUEUE-TOKEN: ${token}`

**Response**
- 성공 (http status code: 200)
```json
{
  "statusCode": 200,
  "data": {
    "tokenId": 20,
    "remainQueueCount": 1,
    "waitingQueueStatus": "WAITING",
    "expiredDate": "2024-10-11T06:37:10.4898949"
  }
}
```
- 실패 - 존재하지 않은 토큰 (http status code: 404)
```json
{
  "code": "token_err_1",
  "message": "token not found"
}
```
- 실패 - 유효하지 않은 토큰 (http status code: 400)
```json
{
  "code": "token_err_2",
  "message": "invalid token"
}
```


### 사용자

### 1. 계좌 정보 조회

- **HTTP Method**: `GET`
- **URL**: `/api/v1/users/{userId}/accounts`
- **Summary**: 계좌 정보 조회
- **Description**: 사용자 ID를 통해 해당 계좌 정보를 조회합니다.
- **Path Parameters**:
    - `userId` (String, Required): 조회할 사용자의 ID.
  - **Response**:
      - **200 OK**: 요청이 성공적으로 처리된 경우, 사용자 계좌 정보가 반환됩니다.
      ```json
  
      {
        "statusCode": 200,
        "data": {
          "userId": 1,
          "accountId": 10,
          "balance": 10000,
          "createdAt": "2024-10-01T06:38:18.098663",
          "updatedAt": "2024-10-06T06:38:18.098663"
         }
      }
      ```
    
      - **404 NOT FOUND**: 사용자 ID로 계좌를 찾을 수 없는 경우.
      ```json
      {
        "code": "user_err_1",
        "message": "user not found"
      }
      ```
---

### 2. 계좌 잔액 충전

- **HTTP Method**: `PATCH`
- **URL**: `/api/v1/users/{userId}/accounts`
- **Summary**: 계좌 잔액 충전
- **Description**: 사용자 ID를 통해 해당 계좌에 잔액을 충전합니다.
- **Path Parameters**:
    - `userId` (String, Required): 충전할 사용자의 ID.
- **Request Body**:
    - **UserRequest.ChargeAccount** (Required): 충전 정보 요청 객체.
    ```json
    {
      "balance": 50000
    }
    ```
    - `amount` (Integer, Required): 충전할 금액.
- **Response**:
    - **200 OK**: 요청이 성공적으로 처리된 경우, 사용자 계좌 정보가 반환됩니다.
    ```json
    {
      "statusCode": 200,
      "data": {
        "userId": 1,
        "accountId": 10,
        "balance": 20000,
        "createdAt": "2024-10-01T06:43:03.4057779",
        "updatedAt": "2024-10-11T06:43:03.4057779"
        }
    }
    ```
    - **404 NOT FOUND**: 사용자 ID로 계좌를 찾을 수 없는 경우.
    ```json
    {
      "code": "user_err_1",
      "message": "User not found"
    }
    ```
    - **400 BAD REQUEST**: 요청이 잘못된 경우 (예: 금액이 유효하지 않은 경우).
    ```json
    {
      "code": "user_err_2",
      "message": "Invalid amount"
    }
    ```
---

### 콘서트 

---

### 1. 콘서트 목록 조회 API

- **HTTP Method**: `GET`
- **URL**: `/concerts`
- **Summary**: 콘서트 목록 조회
- **Description**: 콘서트 목록을 조회합니다.
- **Query Parameters**:
    - `token` (String, Optional, Hidden): 인증 토큰. (기본적으로 숨겨짐)
- **Response**:
    - **200 OK**: 요청이 성공적으로 처리된 경우, 콘서트 목록이 반환됩니다.
    ```json
    {
      "statusCode": 200,
      "data": [
        {
          "concertId": 1,
          "concertName": "싸이 콘서트",
          "createdAt": "2024-10-11",
          "deletedAt": null
        },
        {
          "concertId": 2,
          "concertName": "빅뱅 콘서트",
          "createdAt": "2024-10-11",
          "deletedAt": null
        }
      ]
    }
    ```
    - **401 UNAUTHORIZED**: 대기열 인증 실패.
    ```json
    {
      "code": "token_err_1",
      "message": "token not found"
    }
    ```
    ```json
    {
      "code": "token_err_2",
      "message": "invalid"
    }
    ```
---

### 2. 예약 가능한 콘서트 날짜 조회 API

- **HTTP Method**: `GET`
- **URL**: `/concerts/{concertId}/dates`
- **Summary**: 예약 가능한 콘서트 날짜 조회
- **Description**: 예약 가능한 콘서트 날짜 목록을 반환합니다.
- **Path Parameters**:
    - `concertId` (Long, Required): 조회할 콘서트의 ID.
- **Request Body**:
    - **ConcertRequest.SearchDate**: 날짜 검색 조건 객체.
    ```json
    {
      "from": "2024-11-01T00:00:00",
      "to": "2024-11-02T00:00:00"
    }
    ```
    - `from` (LocalDatetime, Optional): 조회 시작 날짜.
    - `to` (LocalDatetime, Optional): 조회 종료 날짜.
- **Query Parameters**:
    - `token` (String, Optional, Hidden): 인증 토큰.
- **Response**:
    - **200 OK**: 요청이 성공적으로 처리된 경우, 예약 가능한 콘서트 날짜 목록이 반환됩니다.
    ```json
    {
       "statusCode": 200,
       "data": [
         {
          "concertDateId": 1,
          "concertDate": "2024-10-11T06:53:41.7625346",
          "startTime": "2024-10-11T07:53:41.7625346",
          "endTime": "2024-10-11T08:53:41.7625346"
         },
          {
          "concertDateId": 2,
          "concertDate": "2024-11-11T06:53:41.7625346",
          "startTime": "2024-11-11T07:53:41.7625346",
          "endTime": "2024-11-11T08:53:41.7625346"
         }
        ]
    }
    ```
    - **401 UNAUTHORIZED**: 대기열 인증 실패.
      ```json
      {
        "code": "token_err_1",
        "message": "token not found"
      }
      ```
      ```json
      {
        "code": "token_err_2",
        "message": "invalid"
      }
      ```
    - **404 NOT FOUND**: 해당 콘서트 ID로 날짜를 찾을 수 없는 경우.
    ```json
    {
      "status": "concert_err_1",
      "message": "Concert not found"
    }
    ```

---

### 3. 예약 가능한 좌석 조회 API

- **HTTP Method**: `GET`
- **URL**: `/concerts/{concertId}/dates/{concertDateId}/seats`
- **Summary**: 예약 가능한 좌석 조회
- **Description**: 예약 가능한 좌석 목록을 반환합니다.
- **Path Parameters**:
    - `concertId` (Long, Required): 조회할 콘서트의 ID.
    - `concertDateId` (Long, Required): 조회할 콘서트 날짜의 ID.
- **Query Parameters**:
    - `token` (String, Optional, Hidden): 인증 토큰.
- **Response**:
    - **200 OK**: 요청이 성공적으로 처리된 경우, 예약 가능한 좌석 목록이 반환됩니다.
    ```json
    {
      "statusCode": 200,
      "data": [
        {
         "seatId": 1,
        "seatNo": 5,
        "price": 50000
       },
       {
        "seatId": 2,
        "seatNo": 3,
        "price": 30000
        }
       ]
     }
    ```
    - **401 UNAUTHORIZED**: 대기열 인증 실패.
        ```json
        {
          "code": "token_err_1",
          "message": "token not found"
        }
        ```
        ```json
        {
          "code": "token_err_2",
          "message": "invalid"
        }
        ```
    - **404 NOT FOUND**: 해당 날짜에 좌석을 찾을 수 없는 경우.
    ```json
    {
      "status": "concert_err_3",
      "message": "Seats not found"
    }
    ```

---

### 4. 좌석 예약 API

- **HTTP Method**: `POST`
- **URL**: `/concerts/{concertId}/dates/{concertDateId}/reservations`
- **Summary**: 좌석 예약
- **Description**: 콘서트 날짜 ID와 좌석 정보를 통해 좌석을 예약 후 예약 정보를 반환합니다.
- **Path Parameters**:
    - `concertId` (Long, Required): 예약할 콘서트의 ID.
    - `concertDateId` (Long, Required): 예약할 콘서트 날짜의 ID.
- **Request Body**:
    - **ConcertRequest.Reservation**: 좌석 예약 요청 객체.
    ```json
    {
      "seatId": 201
    }
    ```
    - `seatId` (Long, Required): 예약할 좌석의 ID.
- **Query Parameters**:
    - `token` (String, Optional, Hidden): 인증 토큰.
- **Response**:
    - **200 OK**: 요청이 성공적으로 처리된 경우, 좌석 예약 정보가 반환됩니다.
    ```json
    {
      "statusCode": 200,
      "data": {
        "concertReservationId": 10
      }
    }
    ```
    - **400 BAD REQUEST**: 좌석이 이미 예약되었거나 유효하지 않은 경우.
    ```json
    {
      "status": "concert_err_4",
      "message": "Seat already reserved"
    }
    ```

### 결제   


---

### 결제 요청 API

- **HTTP Method**: `POST`
- **URL**: `/payments`
- **Summary**: 결제 요청
- **Description**: 예약 ID를 통해 결제를 완료합니다.
- **Request Body**:
  - **PaymentRequest.Create**: 결제 요청 객체.
    ```json
    {
      "reservationId": 401
    }
    ```
  - `reservationId` (Long, Required): 결제할 예약의 ID.
  - `paymentMethod` (String, Required): 결제 방법 (예: "CREDIT_CARD", "BANK_TRANSFER").
  - `amount` (Integer, Required): 결제 금액.
- **Query Parameters**:
  - `token` (String, Optional, Hidden): 인증 토큰.
- **Response**:
  - **200 OK**: 결제가 성공적으로 완료된 경우, 결제 정보가 반환됩니다.
    ```json
    {
      "statusCode": 200,
      "data": {
        "paymentId": 501,
        "concertId": 40,
        "concertName": "싸이 콘서트",
        "seatNo": 50,
        "price": 50000,
        "completedAt": "2024-10-11T07:02:11.5333424"
      }
    }
    ```
  - **400 BAD REQUEST**: 결제 요청이 잘못된 경우 (예: 예약 ID가 유효하지 않음).
    ```json
    {
      "status": "concert_err_5",
      "message": "Invalid reservation ID or amount"
    }
    ```
  - **401 UNAUTHORIZED**: 대기열 인증 실패.
    ```json
    {
      "code": "token_err_1",
      "message": "token not found"
    }
    ```
    ```json
    {
      "code": "token_err_2",
      "message": "invalid"
    }
    ```
  - **404 NOT FOUND**: 해당 예약을 찾을 수 없는 경우.
    ```json
    {
      "status": "concert_err_6",
      "message": "Reservation not found"
    }
    ```


