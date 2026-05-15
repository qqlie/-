package ua.fitness.service;

public interface Persistable {

    void save();

    void load(int id);
}
