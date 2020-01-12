/*
 * Copyright (C) 2019 The LineageOS Project
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
package org.lineageos.frameratetile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemProperties;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences =
                context.createDeviceProtectedStorageContext().getSharedPreferences(
                        Constants.FRAME_RATE_TILE,
                        Context.MODE_PRIVATE);
        int lastFps = sharedPreferences.getInt(Constants.LAST_FPS, 60);
        if (lastFps == 120) {
            // Edge case because default fps is 60 but you can't jump
            // from 60 to 120 so go to 90 first and then to 120
            SystemProperties.set(Constants.VENDOR_FPS, Integer.toString(90));
            // Wait a little bit until setting the prop the second time
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
            SystemProperties.set(Constants.VENDOR_FPS, String.valueOf(lastFps));
        } else {
            SystemProperties.set(Constants.VENDOR_FPS, String.valueOf(lastFps));
        }
    }
}
