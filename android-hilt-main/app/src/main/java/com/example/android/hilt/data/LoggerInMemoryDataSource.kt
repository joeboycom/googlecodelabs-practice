/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.example.android.hilt.data

import dagger.hilt.android.scopes.ActivityScoped
import java.util.LinkedList
import javax.inject.Inject

/*
* Since our application consists of only one Activity (also called a single-Activity application),
* we should have an instance of the LoggerInMemoryDataSource in the Activity container and reuse that instance across Fragments.
*
* https://developer.android.com/training/dependency-injection/hilt-android#component-scopes
* We can achieve the in-memory logging behavior by scoping LoggerInMemoryDataSource to the Activity container:
* every Activity created will have its own container, a different instance.
* On each container, the same instance of LoggerInMemoryDataSource will be provided when the logger is needed as a dependency or for field injection.
* Also, the same instance will be provided in containers below the Components hierarchy.
* */
@ActivityScoped
class LoggerInMemoryDataSource @Inject constructor() : LoggerDataSource {

    private val logs = LinkedList<Log>()

    override fun addLog(msg: String) {
        logs.addFirst(Log(msg, System.currentTimeMillis()))
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        callback(logs)
    }

    override fun removeLogs() {
        logs.clear()
    }
}
