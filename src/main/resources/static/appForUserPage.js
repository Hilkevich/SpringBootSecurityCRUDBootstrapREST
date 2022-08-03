
$(async function () {
    await getDataForUserPage();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },

     //получаем аутентифицированного юзера из UserRestController
    getAuthenticationUser:async() => await fetch('/user-authentication')
}

//заполняем navbar и таблицу в userPage.html
//сработало без ошибок, только при выводе этого метода в отдельный js файл! а не внутри app.js!

async function getDataForUserPage() {
    let tableUserPage = $('#tableUserPage tbody');
    let navbarUserPage = $('#navbarUserPage');
    tableUserPage.empty();                           // очищаем tbody таблицы, столбцы остаются
    navbarUserPage.empty();                          // очищаем navbar

    //обращаемся в наш REST GET контроллер getAuthenticationUser по адресу '/user-authentication'.
    await userFetchService.getAuthenticationUser()
        .then(res => res.json())
        .then(user => {
            let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.surname}</td>

                             <!--заполнение ролями в столбце Role-->
                            <td>${user.roles.map(role => role.name.substring(5))}</td>
                        </tr>
                )`;

            //заполняем таблицу tableUserPage (в userPage.html)
            tableUserPage.append(tableFilling);

            //заполняем navbar (в userPage.html)
            navbarUserPage.append(user.username + ' with roles: ' + user.roles.map(role => role.name.substring(5)));
        })
}