/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.cache.internal.locklistener;

import java.io.File;

/**
 * By Szczepan Faber on 5/28/13
 */
public class NoOpFileLockListener implements FileLockListener {

    public void lockCreated(File target, Runnable whenContended) {}

    public void stopListening(File target) {}

    public int reservePort() {
        return -1;
    }
}