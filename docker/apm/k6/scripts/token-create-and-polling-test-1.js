import http from 'k6/http';
import { sleep, check } from 'k6';
import { Counter } from 'k6/metrics';

const createFail = new Counter('create_fail_count');
const activeSuccess = new Counter('active_success_count');
const activeFailure = new Counter('active_failure_count');

const vuStatus = Array(1000).fill(false);

let completedVUCount = 0;

export const options = {
    scenarios: {
        // s1: {
        //     exec: 'user_waiting_tokens_process',
        //     executor: 'per-vu-iterations',
        //     vus: 1000,
        //     iterations: 1,
        //     maxDuration: '3m',
        //     tags: { scenario: 's1' },
        // },
        s2: {
            exec: 'user_waiting_tokens_process',
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '20s', target: 5000 }, // 20초 동안 5000명으로 증가
                { duration: '10s', target: 6000 }, // 10초 동안 6000명으로 증가
                { duration: '1m', target: 0 }, // 마지막 1분 동안 0으로 감소
            ],
            gracefulStop: '0s',
            gracefulRampDown: '0s',
            tags: { scenario: 's2' },
        },
    },
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)']
};

export function user_waiting_tokens_process () {
    const vuIndex = __VU - 1;
    const baseUrl = 'http://concert-reservation-app:8080/api/v1/waiting-queues';
    // Step 1: 사용자가 토큰 생성을 요청
    const token = createWaitingToken(baseUrl)
    if (!token) {
        console.error(`VU ${vuIndex} - Failed to create token`);
        createFail.add(1);
        updateCompletedVU();
        return
    }
    // Step 2: 토큰 순번 체크 요청
    let active = false;
    let attempts = 0;
    while (attempts < 50) {
        const order = getTokenOrder(baseUrl, token)
        if (order === 0) {
            active = true;
            activeSuccess.add(1);
            vuStatus[vuIndex] = true;

            console.log(`VU ${vuIndex} - Token ${token} is active after ${attempts + 1} attempts -> state[${vuStatus[vuIndex]}`);
            return;
        } else {
            attempts++;
            sleep(5);
        }
    }
    console.error(`VU ${vuIndex} - Failed to become active after ${attempts} attempts`);
    vuStatus[vuIndex] = true;

    updateCompletedVU();

}

function createWaitingToken(baseUrl) {
    const response = http.post(`${baseUrl}`);
    check(response, {
        '대기열 토큰 생성': (r) => r.status === 200,
    });

    return JSON.parse(response.body).data.token;
}

function getTokenOrder(baseUrl, token) {
    const response = http.get(`${baseUrl}/tokens`, {
        headers: {'X-Queue-Token': token, 'Content-type': 'application/json'},
    });

    check(response, {
        '대기열 토큰 순번 조회': (r) => r.status === 200,
    });

    return JSON.parse(response.body).data.order
}

function updateCompletedVU() {
    completedVUCount++;
    console.log(`current completed VU Count: ${completedVUCount}`);
    if (completedVUCount === options.vus) {
        console.log(`All VUs completed! Total: ${completedVUCount}`);
    }
}

export function teardown() {
    const incompleteVUCount = vuStatus.length - activeSuccess.count

    console.log(`## Incomplete VUs: ${incompleteVUCount}`);
    activeFailure.add(incompleteVUCount);
}
