/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.foodtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodtracker.database.foodlog.FoodlogDao
import com.example.foodtracker.database.category.Category
import com.example.foodtracker.database.category.CategoryDao
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.database.ontlasting.Ontlasting
import com.example.foodtracker.database.ontlasting.OntlastingDao
import com.example.foodtracker.database.period.Period
import com.example.foodtracker.database.period.PeriodDao
import com.example.foodtracker.database.product.Product
import com.example.foodtracker.database.product.ProductDao
//import com.example.foodtracker.database.symptom.Symptom
//import com.example.foodtracker.database.symptom.SymptomDao


/**
 * Defines a database and specifies data tables that will be used.
 * Version is incremented as new tables/columns are added/removed/changed.
 * You can optionally use this class for one-time setup, such as pre-populating a database.
 */

/** @Database(entities = arrayOf(Product::class, Category::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/foodtracker.db")
                    .allowMainThreadQueries().build()
                INSTANCE = instance

                instance
            }
        }
    }
}
*/
//@Database(entities = [Category::class, Product::class, Consume::class], version = 1, exportSchema = false)
@Database(entities = [Category::class, Product::class, Consume::class, Complaint::class, Ontlasting::class, Period::class], version = 6,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun consumeDao(): ConsumeDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun ontlastingDao(): OntlastingDao
    abstract fun periodDao(): PeriodDao
    abstract fun foodlogDao(): FoodlogDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                CREATE TABLE complaint (
                    id INTEGER PRIMARY KEY NOT NULL,
                    symptom TEXT NOT NULL,
                    start_datetime TEXT NOT NULL,
                    end_datetime TEXT NOT NULL,
                    severeness INTEGER NOT NULL,
                    description TEXT NOT NULL
                )
                """.trimIndent())
            }
        }
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                CREATE TABLE ontlasting (
                    id INTEGER PRIMARY KEY NOT NULL,
                    start_datetime TEXT NOT NULL,
                    type TEXT NOT NULL
                )
                """.trimIndent())
                database.execSQL("""
                CREATE TABLE period (
                    id INTEGER PRIMARY KEY NOT NULL,
                    start_datetime TEXT NOT NULL,
                    end_datetime TEXT NOT NULL
                )
                """.trimIndent())
            }
        }
        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                ALTER TABLE consume
                    ADD pill INTEGER NOT NULL DEFAULT 0
                """.trimIndent())
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, AppDatabase::class.java, "foodtracker")
                            .createFromAsset("foodtracker.db")
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .allowMainThreadQueries()
                            .build()
                            //.also {
                            //    Log.d("<DEV>", it.openHelper.writableDatabase.path)
                            //}
                }
            }
            return INSTANCE!!
        }
        fun destroyInstance() {

            if (INSTANCE?.isOpen == true) {
                INSTANCE?.close()
            }

            INSTANCE = null
        }
    }


}