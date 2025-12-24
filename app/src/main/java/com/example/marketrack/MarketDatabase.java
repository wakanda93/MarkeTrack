package com.example.marketrack;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Product.class, SaleTransaction.class}, version = 2, exportSchema = false)
public abstract class MarketDatabase extends RoomDatabase {

    public abstract ProductDao productDao();

    private static volatile MarketDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // This ExecutorService satisfies the "Background Thread" requirement
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MarketDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MarketDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MarketDatabase.class, "market_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}