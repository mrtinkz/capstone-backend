package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.ShoppingCart;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.ShoppingCart}.
 */
public interface ShoppingCartService {
    /**
     * Save a shoppingCart.
     *
     * @param shoppingCart the entity to save.
     * @return the persisted entity.
     */
    ShoppingCart save(ShoppingCart shoppingCart);

    /**
     * Updates a shoppingCart.
     *
     * @param shoppingCart the entity to update.
     * @return the persisted entity.
     */
    ShoppingCart update(ShoppingCart shoppingCart);

    /**
     * Partially updates a shoppingCart.
     *
     * @param shoppingCart the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShoppingCart> partialUpdate(ShoppingCart shoppingCart);

    /**
     * Get all the shoppingCarts.
     *
     * @return the list of entities.
     */
    List<ShoppingCart> findAll();

    /**
     * Get the "id" shoppingCart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShoppingCart> findOne(Long id);

    /**
     * Delete the "id" shoppingCart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
