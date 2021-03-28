package concurs.persistence;

import concurs.model.Entitate;

public interface IRepository<ID, E extends Entitate<ID>> {

    public E findOne(ID id);

    public Iterable<E> findAll();

    public void save(E entity);

    public void delete(ID id);

    public void update(E entity);

}
