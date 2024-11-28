import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';  // Random 값 생성

// 사용자 수 및 테스트 기간 설정
const USER_ID_RANGES = 10;  // 1 ~ 1000 사이 사용자 ID
const CONCERT_ID = 1;
const CONCERT_SCHEDULES_ID = 1;
const SEAT_ID_RANGES = 50;

const BASE_URL = 'http://concert-reservation-app:8080/api/v1';

export let options = {
    scenarios: {
        payment_test: {
            exec: 'reservation_payment_process',
            executor: 'per-vu-iterations',
            vus: 10,
            iterations: 1,
            maxDuration: '1m',

        }
    },
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

export function reservation_payment_process () {
    // 랜덤하게 userId, seatId를 생성
    const userId = randomIntBetween(1, 10);
    const concertScheduleId = CONCERT_SCHEDULES_ID;
    const seatId = randomIntBetween(1, SEAT_ID_RANGES);

    console.log(`### 예약 및 결제 요청 - 사용자 ID: ${userId}, 스케쥴 ID: ${concertScheduleId}, 좌석 ID: ${seatId}`);

    // 대기열 토큰
    const token = "k6TestToken"
    // 예약 요청
    const reservationResponse = createReservation(BASE_URL, CONCERT_ID, concertScheduleId, seatId, userId, token);
    const reservationId = reservationResponse.json('data.reservationId');
    if (!reservationId) {
        console.error(`### 예약 실패 - 사용자 ID: ${userId}, Status: ${reservationResponse.status}, Body: ${reservationResponse.body}`);
        return;
    }


    console.log(`### 예약 완료 - 사용자 ID: ${userId}, 예약 ID: ${reservationId}`);

    // 결제 요청
    const paymentResponse = createPayment(BASE_URL, reservationId, token);
    if (paymentResponse.status !== 200) {
        console.error(`### 결제 실패 - 사용자 ID: ${userId}, Status: ${paymentResponse.status}, Body: ${paymentResponse.body}`);
        return;
    }

    const price = paymentResponse.json('data.payAmount');
    const paymentAt = paymentResponse.json('data.createdAt');
    console.log(`### 결제 완료 - 사용자 ID: ${userId}, 결제 금액: ${price}, 결제 일시: ${paymentAt}`);
    sleep(2);
}

function createReservation(baseUrl, concertId, scheduleId, seatId, userId, token) {
    const reservationResponse = http.post(`${baseUrl}/concerts/${concertId}/schedules/${scheduleId}/reservations`,
        JSON.stringify({
            seatId: seatId,
            userId: userId,
        }), {
        headers: {
            'X-Queue-Token': token,
            'Content-Type': 'application/json',
        },
    });

    check(reservationResponse, {
        '예약 요청 성공': (r) => r.status === 200,
    });
    return reservationResponse;
}

function createPayment(baseUrl, reservationId, token) {
    const paymentResponse = http.post(`${baseUrl}/payments`,
        JSON.stringify({
            reservationId: reservationId,
        }), {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'X-Queue-Token': token
            },
        });

    check(paymentResponse, {
        '결제 성공': (r) => r.status === 200,
    });
    return paymentResponse;
}
