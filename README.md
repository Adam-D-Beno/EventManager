Проект представляет собой сервис для организации, управления и участия в различных мероприятиях, таких как конференции, семинары, встречи и культурные события. Он позволяет пользователям создавать мероприятия, регистрироваться на них, получать уведомления об изменениях и управлять своим участием. Сервис включает в себя функции для работы с местоположениями мероприятий, управления пользователями и отправки нотификаций.

Функции, которые вам предстоит реализовать:

1. Управление Мероприятиями:
      1.1 Создание, редактирование и удаление мероприятий.
      1.2 Просмотр подробной информации о мероприятиях, включая место проведения, время и список участников.
2. Управление Местами Проведения: CRUD операции для мест проведения мероприятий с возможностью указания детальной информации о местоположении.
3. Регистрация и Управление Пользователями:
      3.1 Регистрация, аутентификация и управление профилями пользователей.
      3.2 Назначение ролей и прав доступа.
4. Участие в Мероприятиях: Возможность регистрации на мероприятия и отмены участия.
5. Уведомления: Получение уведомлений о предстоящих мероприятиях, изменениях в расписании или месте проведения.
6. Поиск и Фильтрация Мероприятий: Поиск мероприятий по ключевым словам, дате, местоположению и другим параметрам.

📝 Стек технологий
• Бэкенд: Java 21, Spring Boot (включая Spring MVC, Spring Data JPA, Spring Security), Hibernate для ORM.
• База Данных: PostgreSQL для хранения данных о пользователях, мероприятиях и местах проведения.
• Аутентификация и Авторизация: JWT для аутентификации и авторизации пользователей.
• Сообщения и Уведомления: Apache Kafka для асинхронного обмена сообщениями между сервисами

Дополнительные технологии:
Swagger и OpenAPI для документации REST API.
Postman для отправки запросов
Docker для поднятия PostgreSQL и Kafka локально у себя на машине
