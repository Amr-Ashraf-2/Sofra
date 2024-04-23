package com.ashraf.amr.apps.sofra.data.local.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert
    void insertItemToCar(ProductsData... productData);

    @Update
    void updateItemToCar(ProductsData... productData);

    @Delete
    void deleteItemToCar(ProductsData... productData);

    @Query("Delete from ProductsData")
    void deleteAllItemToCar();

    @Query("Select * from ProductsData")
    List<ProductsData> getAllItem();
}
