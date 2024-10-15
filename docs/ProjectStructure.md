## 아키텍쳐 구조

```shell
concert
    ├─application
    │  └─payment
    ├─domain
    │  ├─common
    │  │  └─exception
    │  ├─concert
    │  │  ├─exception
    │  │  ├─model
    │  │  ├─repository
    │  │  └─service
    │  ├─payment
    │  ├─user
    │  └─waitingqueue
    ├─infra
    │  └─db
    │     ├─concert
    │     ├─payment
    │     ├─user
    │     └─waitingqueue
    └─interfaces
       └─api
         ├─common
         │  ├─config
         │  └─advice
         ├─concert
         ├─payment
         ├─user
         └─waitingqueue
      
```


