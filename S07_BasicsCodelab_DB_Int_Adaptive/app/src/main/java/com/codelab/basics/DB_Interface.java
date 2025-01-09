package com.codelab.basics;

import java.util.List;

public interface DB_Interface {

    int count();

    int save(PokeModel pokeModel);

    void update(PokeModel pokeModel);

    List<PokeModel> findAll();

    int deleteById(Long id);

    String getNameById(Long id);

}
