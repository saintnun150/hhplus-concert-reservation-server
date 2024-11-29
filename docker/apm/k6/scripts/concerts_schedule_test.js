import http from 'k6/http';
import { sleep, check } from 'k6';
import { Counter } from 'k6/metrics';

const createFail = new Counter('create_fail_count');
const activeSuccess = new Counter('active_success_count');
const activeFailure = new Counter('active_failure_count');

export const options = {
    scenarios: {
        s1: {
            exec: 'select_concert_schedule_query',
            executor: 'per-vu-iterations',
            vus: 1,
            iterations: 1,
            maxDuration: '1m',
            tags: {scenario: 's1'},
        },
        // s2: {
        //     exec: 'user_waiting_tokens_process',
        //     executor: 'ramping-vus',
        //     startVUs: 0,
        //     stages: [
        //         { duration: '1m', target: 3000 }, // 1분 동안 3000명으로 증가
        //         { duration: '3m', target: 3000 }, // 3분 동안 3000명으로 유지
        //         { duration: '1m', target: 4000 }, // 1분 동안 4000으로 증가
        //         { duration: '1m', target: 1000 }, // 1분 동안 1000으로 감소
        //         { duration: '1m', target: 0 }, // 마지막 1분 동안 0으로 감소
        //     ],
        //     gracefulStop: '0s',
        //     gracefulRampDown: '0s',
        //     tags: { scenario: 's2' },
        // },
    },
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

export function select_concert_schedule_query () {
    const vuIndex = __VU - 1;
    const baseUrl = 'http://concert-reservation-app:8080/api/v1/'
    const endpoint = 'waiting-queues'
    // Step 1: 사용자가 토큰 생성을 요청
    const token = createWaitingToken(baseUrl+endpoint)
    if (!token) {
        console.error(`VU ${vuIndex} - Failed to create token`);
        createFail.add(1);
        return
    }
    // Step 2: 콘서트 목록 조회
    const concertName = "에스파_콘서트"
    const openedAtFrom = "2024-12-20'T'00:00:00"
    const openedAtTo = "2024-12-31'T'00:00:00"

    const concertInfo = {
        concertName,
        openedAtFrom,
        openedAtTo
    };

    const response = getConcerts(baseUrl, token, concertInfo)
    console.log('## response.json()', response.json())

}

function createWaitingToken(baseUrl) {
    const response = http.post(`${baseUrl}`);
    check(response, {
        '대기열 토큰 생성': (r) => r.status === 200,
    });

    return JSON.parse(response.body).data.token;
}

function getConcerts(baseUrl, token, concertInfo) {
    const queryParams = [];
    for (const key in concertInfo) {
        if (concertInfo.hasOwnProperty(key)) {
            queryParams.push(`${encodeURIComponent(key)}=${encodeURIComponent(concertInfo[key])}`);
        }
    }
    const queryString = queryParams.join('&');
    const url = `${baseUrl}concerts?${queryString}`;

    const response = http.get(url, {
        headers: {'X-Queue-Token': token, 'Content-type': 'application/json'},
    });
    return response;
}

