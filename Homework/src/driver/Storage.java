package driver;

import java.util.List;


public interface Storage<T> {
    List<T> findAll();

    List<T> findAllByAge(Integer age);
}

