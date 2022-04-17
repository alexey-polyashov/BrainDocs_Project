![This is an image](logo.png)
# BrainDocs

## Описание
Это приложение для хранения документов и работы с ними

- Все документы в одном месте
- Доступ к документам через браузер и через мобильные устройства
- Быстрый поиск документов по различным критериям
- Упрощение обработки документов (подпись, согласование, исполнение) и взаимодействие между исполнителями
- Контроль за исполнением задач по обработке документов
- Упрощение заполнения формализованных документов по шаблонам
- Возможность отправки документов по почте

[Видео презентация](https://disk.yandex.ru/i/yczE4gDZdMgwCA)

Проект опубликован на порталах heroku(бэк) и netlify(фронт)

[Работающее приложение](https://angry-noether-64357b.netlify.app/search-doc)

[Описание API](https://brain-docs.herokuapp.com/)

## Что реализовано на текущий момент

- Разработаны основные справочники для ведения списка организаций, видов документов и пользователей
- Разработана форма создания/редактирования документов с возможностью загрузки файлов
- Разработана форма поиска документов по полям документа и по видам документов
- Разработан функционал по работе с задачами:
      - Создание
      - Редактирование
      - Выбор исполнителей
- Отправка уведомлений по задачам (добавлен исполнитель в задачу, добавлен комментарий в задачу)
- Реализована авторизация и регистрация новых пользователей через запрос на регистрацию

## Что в планах

- Добавить подтверждение регистрации 
- Сделать форму задач более удобной
- Доработать механизм логирования действий пользователя
- Добвавить в виды документов настройки для описания полей документа
- Добавить механизм заполнения docx шаблонов документа данными из полей документа
- Добавить отправку документа по почте в виде письма с вложенными файлами
- Добавить возможность подписания электронной цифровой подписью
