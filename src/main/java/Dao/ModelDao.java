package Dao;

import Models.Model;

import java.util.HashSet;

public interface ModelDao<T extends Model> extends Dao<T> {

    // insert one model to database
    T insertOne(T model);

    // delete model from database
    boolean delete(T model);

    // update model in database
    void update(T model);

    // select all models from database
    HashSet<T> selectAll();

}
