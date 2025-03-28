# Task-management-system-test-task
Для запуска приложения, если использовать докер, то:
1. Зайти в папку [dockerfiles](DockerFiles ) через консоль
2. Запустить команду docker-compose up


<ul>
<li>Бд будет доступно по http://localhost:5222</li>

<li>Приложение по http://localhost:8000</li> 

<li>Сваггер будет доступен по http://localhost:8000/swagger-ui/index.html#/ с логином user и паролем 1234</li>

<li>Будут пресозданы админ приложения с логином admin@mail.ru, паролем: 1234 и юзер с логином user@mail.ru, паролем: 1234</li>

</ul>

Если что-то пошло не так, то стоит обратить внимание не заняты ли внешние порты другими приложениями и изменить их. Либо же в файле [.env](/DockerFiles/.env) поменять SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5222/task_management" на SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5222/task_management"

Чтобы запуститься без докера, нужно зайти в консоль postgres и запустить [init.sql](DockerFiles/init.sql)
<br>После чего установить переменные окружения взяв их из файла [.env](DockerFiles/.env) <li>DATASOURCE_USERNAME, DATASOURCE_PASSWORD будут соответствовать SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD
<br>Запуститься. Приложение будет доступно по localhost:8080