package it.unicam.cs.mpgc.rpg125950.lupusintabula.util;

public final class AutoMapper<E extends IEntityMarker, V extends IViewModelMarker> implements IAutoMapper<E, V> {
    /**
     * @param entityViewModel Target view model
     * @return The resulting entity
     */
    @Override
    public E toEntity(V entityViewModel) {
        return null;
    }

    /**
     * @param entity Target entity
     * @return The resulting viewmodel
     */
    @Override
    public V toViewModel(E entity) {
        return null;
    }
}
