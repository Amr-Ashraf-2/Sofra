package com.ashraf.amr.apps.sofra.data.local.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;

@Database(entities = {ProductsData.class}, version = 1, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class RoomManger extends RoomDatabase {

    private static RoomManger roomManger;

    public abstract RoomDao roomDao();

    public static synchronized RoomManger getInstance(Context context) {
        if (roomManger == null) {
            roomManger = Room.databaseBuilder(context.getApplicationContext(), RoomManger.class,
                    "Sofra_v2")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomManger;
    }

}
