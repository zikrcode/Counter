/*
 * Copyright (C) 2023–2026 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zikrcode.counter.data.data_source

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.zikrcode.counter.data.data_source.entity.CounterEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

private const val TEST_DB = "migration_test_counter_database"

@RunWith(AndroidJUnit4::class)
class CounterDatabaseMigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        CounterDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2_copiesRenamedColumnsAndBackfillsTimestamps() {
        val insertedDate = 1_700_000_000_000L

        helper.createDatabase(TEST_DB, 1).use { db ->
            db.execSQL(
                "INSERT INTO counter " +
                    "(id, counter_name, counter_description, counter_date, counter_saved_value) " +
                    "VALUES (1, 'Tasbih', 'Morning dhikr', $insertedDate, 33)"
            )
            db.execSQL(
                "INSERT INTO counter " +
                    "(id, counter_name, counter_description, counter_date, counter_saved_value) " +
                    "VALUES (2, 'Laps', 'Pool laps', ${insertedDate + 1000}, 7)"
            )
        }

        // Also validates the migrated database against the schema derived from the entities.
        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        db.query("SELECT * FROM counter ORDER BY id").use { cursor ->
            assertEquals(2, cursor.count)

            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(cursor.getColumnIndexOrThrow("id")))

            // Renamed columns keep their values.
            assertEquals("Tasbih", cursor.getString(cursor.getColumnIndexOrThrow("name")))
            assertEquals(
                "Morning dhikr",
                cursor.getString(cursor.getColumnIndexOrThrow("description"))
            )
            assertEquals(33, cursor.getInt(cursor.getColumnIndexOrThrow("value")))

            // The old names are gone.
            assertEquals(-1, cursor.getColumnIndex("counter_name"))
            assertEquals(-1, cursor.getColumnIndex("counter_description"))
            assertEquals(-1, cursor.getColumnIndex("counter_date"))
            assertEquals(-1, cursor.getColumnIndex("counter_saved_value"))

            // New columns take their declared defaults.
            assertEquals(1, cursor.getInt(cursor.getColumnIndexOrThrow("step")))
            assertTrue(cursor.isNull(cursor.getColumnIndexOrThrow("target")))
            assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("loop_on_target")))
            assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("rounds")))
            assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("sort_index")))

            // Both timestamps are backfilled from the only one v1 kept.
            assertEquals(
                insertedDate,
                cursor.getLong(cursor.getColumnIndexOrThrow("created_at"))
            )
            assertEquals(
                insertedDate,
                cursor.getLong(cursor.getColumnIndexOrThrow("updated_at"))
            )

            assertTrue(cursor.moveToNext())
            assertEquals(2, cursor.getInt(cursor.getColumnIndexOrThrow("id")))
            assertEquals("Laps", cursor.getString(cursor.getColumnIndexOrThrow("name")))
            assertEquals(7, cursor.getInt(cursor.getColumnIndexOrThrow("value")))
            assertEquals(
                insertedDate + 1000,
                cursor.getLong(cursor.getColumnIndexOrThrow("created_at"))
            )
        }

        db.query("SELECT count(*) FROM counter_daily_total").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(0, cursor.getInt(0))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To2_dailyTotalsCascadeOnCounterDelete() {
        helper.createDatabase(TEST_DB, 1).use { db ->
            db.execSQL(
                "INSERT INTO counter " +
                    "(id, counter_name, counter_description, counter_date, counter_saved_value) " +
                    "VALUES (1, 'Tasbih', 'Morning dhikr', 1, 33)"
            )
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)
        db.execSQL("PRAGMA foreign_keys = ON")
        db.execSQL("INSERT INTO counter_daily_total VALUES (1, 20000, 33)")

        db.execSQL("DELETE FROM counter WHERE id = 1")

        db.query("SELECT count(*) FROM counter_daily_total").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(0, cursor.getInt(0))
        }
    }

    /**
     * AUTOINCREMENT means SQLite never hands out an id again after the row holding it is deleted.
     * Counter ids are stored outside the database — `last_used_counter_id` today, widget config
     * later — where nothing protects them from pointing at a recycled row.
     */
    @Test
    @Throws(IOException::class)
    fun migratedDatabase_assignsIdsAndNeverReusesThem() {
        helper.createDatabase(TEST_DB, 1).use { db ->
            db.execSQL(
                "INSERT INTO counter " +
                    "(id, counter_name, counter_description, counter_date, counter_saved_value) " +
                    "VALUES (1, 'Tasbih', 'Morning dhikr', 1, 33)"
            )
        }
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2).close()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Room
            .databaseBuilder(context, CounterDatabase::class.java, TEST_DB)
            .addMigrations(MIGRATION_1_2)
            .build()

        try {
            runBlocking {
                // An unsaved counter carries id 0; Room treats that as "not set".
                database.counterDao.upsertCounter(
                    CounterEntity(name = "Second", description = "", value = 0)
                )
                val assignedId = database.counterDao.allCounters().first()
                    .single { it.name == "Second" }
                    .id
                assertEquals(2, assignedId)

                val second = requireNotNull(database.counterDao.counterById(assignedId).first())
                database.counterDao.deleteCounter(second)

                database.counterDao.upsertCounter(
                    CounterEntity(name = "Third", description = "", value = 0)
                )
                val thirdId = database.counterDao.allCounters().first()
                    .single { it.name == "Third" }
                    .id
                assertNotEquals("id of a deleted counter was handed out again", assignedId, thirdId)
            }
        } finally {
            database.close()
        }
    }

    /**
     * The tap path and the editor both have to survive a save without destroying history. This is
     * the regression guard for the REPLACE trap: an @Insert(REPLACE) here would delete the parent
     * row and cascade the daily totals away.
     */
    @Test
    @Throws(IOException::class)
    fun upsertAndTargetedUpdate_doNotDestroyDailyTotals() {
        helper.createDatabase(TEST_DB, 1).use { db ->
            db.execSQL(
                "INSERT INTO counter " +
                    "(id, counter_name, counter_description, counter_date, counter_saved_value) " +
                    "VALUES (1, 'Tasbih', 'Morning dhikr', 1, 33)"
            )
        }
        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2).close()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Room
            .databaseBuilder(context, CounterDatabase::class.java, TEST_DB)
            .addMigrations(MIGRATION_1_2)
            .build()

        try {
            database.openHelper.writableDatabase
                .execSQL("INSERT INTO counter_daily_total VALUES (1, 20000, 33)")

            runBlocking {
                val existing = requireNotNull(database.counterDao.counterById(1).first())

                database.counterDao.upsertCounter(
                    existing.copy(name = "Renamed", value = 66)
                )
                database.counterDao.updateCounterValue(id = 1, value = 99)

                val updated = requireNotNull(database.counterDao.counterById(1).first())
                assertEquals("Renamed", updated.name)
                assertEquals(99, updated.value)

                assertNull(
                    "counterById must emit null for a missing row",
                    database.counterDao.counterById(404).first()
                )
            }

            database.openHelper.writableDatabase
                .query("SELECT total FROM counter_daily_total WHERE counter_id = 1")
                .use { cursor ->
                    assertTrue("daily total was wiped by a save", cursor.moveToFirst())
                    assertEquals(33, cursor.getInt(0))
                }
        } finally {
            database.close()
        }
    }
}
