package com.javarush.task.task36.task3608.model;

import com.javarush.task.task36.task3608.bean.User;
import com.javarush.task.task36.task3608.model.service.UserService;
import com.javarush.task.task36.task3608.model.service.UserServiceImpl;

import java.util.List;

public class MainModel implements Model {
    private ModelData modelData = new ModelData();
    private UserService userService = new UserServiceImpl();
/*
. Выполни рефакторинг класса MainModel. Теперь, когда есть удаленные пользователи, часть методов стала работать неправильно.
    Почти во всех методах, где требуется список пользователей, нужно работать только с активными(живыми) пользователями.
    Вынеси в отдельный приватный метод List<User> getAllUsers() получение списка всех активных пользователей.
    Фильтрация активных пользователей у тебя уже есть - метод List<User> filterOnlyActiveUsers(List<User> allUsers).
    Отрефактори все методы, которые используют список пользователей. Они должны использовать список живых пользователей.
*/
  /*  В методе getAllUsers() нужно получить список всех пользователей у объекта userService используя метод getUsersBetweenLevels(1, 100),
    после чего у объекта userService вызвать метод filterOnlyActiveUsers(List<User>).*/



    private List<User> getAllUsers(){
        return userService.filterOnlyActiveUsers(userService.getUsersBetweenLevels(1,100));
    }


    @Override
    public ModelData getModelData() {
        return modelData;
    }


    @Override
    public void loadUsers() {
        modelData.setUsers(getAllUsers());
        modelData.setDisplayDeletedUserList(false);
    }

    public void loadDeletedUsers() {
        modelData.setUsers( userService.getAllDeletedUsers());
        modelData.setDisplayDeletedUserList(true);
    }

    public void loadUserById(long userId) {
        User user = userService.getUsersById(userId);
        modelData.setActiveUser(user);
    }

    public void deleteUserById(long id) {
        modelData.setActiveUser(userService.getUsersById(id));
        userService.deleteUser(id);
        modelData.setUsers(getAllUsers());
    }

    @Override
    public void changeUserData(String name, long id, int level) {
        modelData.setActiveUser(userService.getUsersById(id));
        userService.createOrUpdateUser(name,id,level);
        modelData.setUsers(getAllUsers());

    }
}
