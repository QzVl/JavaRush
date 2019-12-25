package com.javarush.task.task36.task3608.model;

/* пакете model создай класс FakeModel, реализующий Model. Он нам понадобится на начальном этапе.
         В нем создай поле ModelData modelData, которое инициализируй объектом.*/

import com.javarush.task.task36.task3608.bean.User;

import java.util.ArrayList;
import java.util.List;

public class FakeModel implements Model{
    private ModelData modelData = new ModelData();


    @Override
    public void changeUserData(String name, long id, int level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUserById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadUsers() {
        List <User> users = new ArrayList<>();
        users.add(new User("Artem",1,1));
        users.add(new User("Metra",2,2));
        modelData.setUsers(users);
    }

    @Override
    public void loadUserById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ModelData getModelData() {
        return modelData;
    }

    @Override
    public void loadDeletedUsers() {
        throw new UnsupportedOperationException();
    }
}
