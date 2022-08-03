
//запускаем пробную функцию
// getAllUsersTest()
//
// function getAllUsersTest() {
//     $.getJSON("http://localhost:8080/admin", function (data) {
//         console.log('тест: все пользователи: ' + JSON.stringify(data))
//     });
// }

$(async function () {
    await getTableWithUsers();
    await addNewUser();
    await getTableWithOneAuthenticationUser();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers:  async ()     => await fetch('/admin'),                  //GET all
    findOneUser:   async (id)   => await fetch('/admin/' + id),            //GET one
    addOneNewUser: async (user) => await fetch('/admin',       {method: 'POST',   headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser:    async (user) => await fetch('/admin',       {method: 'PUT',    headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser:    async (id)   => await fetch('/admin/' + id, {method: 'DELETE', headers: userFetchService.head}),
    getAuthenticationUser:async() => await fetch('/user-authentication')
}

// функция заполнения основной таблицы
async function getTableWithUsers() {
    let table = $('#users-table tbody');  // id таблицы из html (id="users-table")
    table.empty();                        // очищаем tbody таблицы, столбцы остаются

    //получаем всех юзеров перейдя в наш GET контроллер
    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.surname}</td> 
                                                       
                             <!--заполнение ролями в столбце Role-->
                            <td>
                              ${user.roles.map(role => role.name.substring(5))}                          
                            </td> 
                                                       
                            <!-- кнопки Edit и Delete в таблице. По клику передаем id этого юзера в соответствующий метод-->
                            <td>           
                            <a class="btn btn-info" style="color:white" id="buttonEdit" data-toggle="modal" onclick="getEdit(${user.id})" role="button">Edit</a>   
                            </td>

                            <td>
                             <a class="btn btn-danger" style="color:white" id="buttonDelete" data-toggle="modal" onclick="dropUser(${user.id})" role="button">Delete</a>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling); // добавляем в таблицу. (в adminPage.html)
            })
        })
}

//POST user
async function addNewUser() {
    //при клике на кнопку 'Add new user' (когда пользователь заполнил поля)
    $('#btnAddNewUser').click(async () => {

       //валидируем заполненные данные формы (id=newUser)

        if ($('#inputUsername')       .val().length < 3) {
            $('#warningInputUsername')     .text("first name not valid").show().fadeOut(3000);

        } else if ($('#inputSurname') .val().length < 3) {
            $('#warningInputSurname')      .text("last name not valid") .show().fadeOut(3000);

        } else if ($('#inputPassword').val().length < 3) {
            $('#warningInputPassword')     .text("password not valid")  .show().fadeOut(3000);

        } else if ($('#inputRoles')   .val().length < 1) {
            $('#warningInputRoles')        .text("roles not valid")     .show().fadeOut(3000);

        } else {

            //собираем вводимые в форму значения в переменную
            let addUser = {};
            addUser.username = $('#inputUsername').val();
            addUser.surname  = $('#inputSurname') .val();
            addUser.password = $('#inputPassword').val();
            addUser.roles    = $('#inputRoles')   .val();

            //отправляем полученные значения на наш POST контроллер
            //он сохранит в базу нового юзера с этими данными
            //всё ок или не ок присвоится const response

            const response = await userFetchService.addOneNewUser(addUser);

            if (response.ok) {

                //показываем таблицу обновив её
                $('#nav-allusers-tab').tab('show');
                await getTableWithUsers();

                //очищаем форму добавления юзера, чтоб при следующем обращении она была пуста
                $('#inputUsername').val('');
                $('#inputSurname') .val('');
                $('#inputPassword').val('');
                $('#inputRoles')   .val('');

            } else {
                await alert('Ошибка добавления юзера!');
            }
        }
    });
}

//GET edit
//принимаем id юзера c конкретной кнопки Edit в таблице
async function getEdit(id) {
    //переходим в GET контроллер и получаем этого юзера из базы
    await userFetchService.findOneUser(id)
        .then(res => res.json())
        .then(user => {
            //заполняем форму его данными (editModal)
            $('#modalEditId')      .val(user.id);
            $('#modalEditUsername').val(user.username);
            $('#modalEditSurname') .val(user.surname);
            $('#modalEditPassword').val(user.password);
            //$('#modalEditRoles')   .val(user.roles.map(role => role.name.substring(5)));

            //показывам форму редактирования с его данными
            $('#editModal').modal('show');
        })
}

//PUT edit
//при клике на кнопку Edit в форме редактирования (editModal)
    $('#updateButton').click(async () => {

            //валидируем данные после возможного редактирования

        if ($('#modalEditUsername')       .val().length < 3) {
            $('#warningModalEditUsername')     .text("first name not valid").show().fadeOut(3000);

        } else if ($('#modalEditSurname') .val().length < 3) {
            $('#warningModalEditSurname')      .text("last name not valid") .show().fadeOut(3000);

        } else if ($('#modalEditPassword').val().length < 3) {
            $('#warningModalEditPassword')     .text("password not valid")  .show().fadeOut(3000);

        } else if ($('#modalEditRoles')   .val().length < 1) {
            $('#warningModalEditRoles')        .text("roles not valid")     .show().fadeOut(3000);

        } else {

            //собираем данные из формы editModal в переменную
            let editUser = {};
            editUser.id       = $('#modalEditId')      .val();
            editUser.username = $('#modalEditUsername').val();
            editUser.surname  = $('#modalEditSurname') .val();
            editUser.password = $('#modalEditPassword').val();
            editUser.roles    = $('#modalEditRoles')   .val();

            //отправляем полученные значения на наш PUT контроллер
            //он сохранит в базу отредактированного юзера с этими данными
            //всё ок или не ок присвоится const response

            const response = await userFetchService.updateUser(editUser);

            if (response.ok) {

                //скрываем editModal
                $('#editModal').modal('hide');

                //показываем таблицу обновив её
                $('#nav-allusers-tab').tab('show');
                await getTableWithUsers();

            } else {
                await alert('Ошибка исправления юзера!');
            }
        }
    });


//DELETE
//получаем id юзера с конкретной кнопки Delete в таблице
async function dropUser(id) {

    //переходим в наш GET контроллер и получаем этого юзера из базы
    await userFetchService.findOneUser(id)
        .then(res => res.json())
        .then(user => {

            //заполняем форму его данными (deleteModal)
            $('#modalDeleteId')       .val(user.id);
            $('#modalDeleteUsername') .val(user.username);
            $('#modalDeleteSurname')  .val(user.surname);

            //показывам форму удаления с его данными
            $('#deleteModal').modal('show');
        })

    //при клике по кнопке Delete в форме удаления (deleteModal)
    $('#deleteButton').click(async () => {

        //отправляем полученные значения на наш DELETE контроллер
        //он удалит из базы юзера с этим id
        //всё ок или не ок присвоится const response

        const response = await userFetchService.deleteUser(id);

        if (response.ok) {

            //скрываем deleteModal
            $('#deleteModal').modal('hide');

            //показываем таблицу обновив её.
            $('#nav-allusers-tab').tab('show');
            await getTableWithUsers();

        } else {
            alert('Ошибка удаления юзера');
        }
    });
}

//функция заполнения таблицы и navbar'a данными 1 залогиневшегося юзера. Вкладка: User. (id="tableUserPanel" в adminPage.html)
async function getTableWithOneAuthenticationUser() {
    let tableUserPanel = $('#tableUserPanel tbody');  // id нашей таблицы из html (id="tableUserPanel")
    let navBar = $('#navbarAdminPage');               // id navbar из html (id="navbarAdminPage")
    tableUserPanel.empty();                           // очищаем tbody таблицы, столбцы остаются
    navBar.empty();

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

            //заполняем таблицу tableUserPanel (в adminPage.html)
            tableUserPanel.append(tableFilling);

            //заполняем navbar (в adminPage.html)
            navBar.append(user.username + ' with roles: ' + user.roles.map(role => role.name.substring(5)));
        })
}
