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

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Rebuilds `counter` with its final column names and adds the daily totals table.
 *
 * SQLite before 3.25 (Android 11 / API 30) has no `ALTER TABLE ... RENAME COLUMN`, and minSdk here
 * is 26, so renaming means recreating the table and copying the rows across. The new table is
 * created with every v2 column at once, which folds the renames, the new columns and the
 * `created_at` backfill into a single copy.
 *
 * Order matters: `counter` is rebuilt *before* `counter_daily_total` exists, so at rebuild time no
 * foreign key points at it and the drop cannot cascade anything away.
 *
 * Room validates the result against the schema derived from the entities immediately afterwards
 * and throws if they disagree, so the statements below are copied verbatim from the generated
 * `2.json`. Columns, defaults and the foreign key clause are all compared. Index names are the one
 * exception — Room treats any two indices whose names start with `index_` as equivalent — but the
 * generated name is used anyway so that upgraded and freshly created databases match exactly.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `counter` RENAME TO `counter_old`")

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `counter` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`name` TEXT NOT NULL, " +
                "`description` TEXT NOT NULL, " +
                "`value` INTEGER NOT NULL, " +
                "`step` INTEGER NOT NULL DEFAULT 1, " +
                "`target` INTEGER, " +
                "`loop_on_target` INTEGER NOT NULL DEFAULT 0, " +
                "`rounds` INTEGER NOT NULL DEFAULT 0, " +
                "`sort_index` INTEGER NOT NULL DEFAULT 0, " +
                "`created_at` INTEGER NOT NULL DEFAULT 0, " +
                "`updated_at` INTEGER NOT NULL DEFAULT 0)"
        )

        // counter_date was written on every save, so it is the last-modified time. It is also the
        // only timestamp v1 kept, which makes it the best available creation time — and the last
        // chance to get one, since it drifts further from the truth with every future edit.
        db.execSQL(
            "INSERT INTO `counter` (`id`, `name`, `description`, `value`, `created_at`, `updated_at`) " +
                "SELECT `id`, `counter_name`, `counter_description`, `counter_saved_value`, " +
                "`counter_date`, `counter_date` FROM `counter_old`"
        )

        db.execSQL("DROP TABLE `counter_old`")

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `counter_daily_total` (" +
                "`counter_id` INTEGER NOT NULL, " +
                "`epoch_day` INTEGER NOT NULL, " +
                "`total` INTEGER NOT NULL, " +
                "PRIMARY KEY(`counter_id`, `epoch_day`), " +
                "FOREIGN KEY(`counter_id`) REFERENCES `counter`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE )"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_counter_daily_total_epoch_day` " +
                "ON `counter_daily_total` (`epoch_day`)"
        )
    }
}
