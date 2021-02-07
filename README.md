# Курсовой проект "Сервис перевода денег"

### Описание проекта 

REST-сервис предоставляет интерфейс для перевода денег с одной карты на другую согласно спецификации: https://github.com/netology-code/jd-homeworks/blob/master/diploma/MoneyTransferServiceSpecification.yaml.

FRONT для сервиса: https://github.com/serp-ya/card-transfer.

### Особенности
- Приложение разработано с использованием Spring Boot.
- В сервисе пока не реализовано подключение к БД (сейчас заглушка в виде внутренней HashMap - значения см. ниже*)  
- Для запуска можно использовать docker, docker-compose

### Запуск (c docker compose)
- создать директорию "services" и поместить в нее репозитории сервиса и FRONT'а.
- в корень фронта поместить Dockerfile, отсюда: https://github.com/ArkhipovMarat/MoneyTransferServiceDockerCompose/tree/master/Dockerfile-front
- в директорию "services" поместить docker-compose, отсюда:
https://github.com/ArkhipovMarat/MoneyTransferServiceDockerCompose/tree/master/Docker-compose
- запустить, выполнив в корневой папке "services" команду: 
_docker-compose up_.

### *данные в хранилище пользователей для тестирования
- _user 1_: 
___номер карты___: 1111111111111111, ___срок действия___: 11/21, ___cvv___: 111, ___сумма___: 1000
- _user 2_: 
___номер карты___: 2222222222222222, ___срок действия___: 11/21, ___cvv___: 111, ___сумма___: 1000
- _user 3_: 
___номер карты___: 3333333333333333, ___срок действия___: 11/21, ___cvv___: 111, ___сумма___: 1000
- _user 4_: 
___номер карты___: 4444444444444444, ___срок действия___: 11/21, ___cvv___: 111, ___сумма___: 1000
