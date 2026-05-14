package it.unicam.cs.mpgc.rpg125950.lupusintabula.util;

sealed public interface IAutoMapper<E extends IEntityMarker, V extends IViewModelMarker> permits AutoMapper {
    E toEntity(V entityViewModel);
    V toViewModel(E entity);
}