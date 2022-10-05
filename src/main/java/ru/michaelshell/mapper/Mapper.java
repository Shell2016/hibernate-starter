package ru.michaelshell.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
