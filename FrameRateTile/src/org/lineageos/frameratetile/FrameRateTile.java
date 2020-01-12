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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.SystemProperties;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class FrameRateTile extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();

        SharedPreferences sharedPreferences =
                createDeviceProtectedStorageContext().getSharedPreferences(
                        Constants.FRAME_RATE_TILE,
                        Context.MODE_PRIVATE);
        Icon icon;
        switch (sharedPreferences.getInt(Constants.LAST_FPS, 60)) {
            case 90:
                icon = Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_90);
                break;
            case 120:
                icon = Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_120);
                break;
            default:
                icon = Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_60);
                break;
        }

        getQsTile().setIcon(icon);
        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        SharedPreferences sharedPreferences =
                createDeviceProtectedStorageContext().getSharedPreferences(
                        Constants.FRAME_RATE_TILE,
                        Context.MODE_PRIVATE);
        int newFps;
        Icon icon;
        switch (sharedPreferences.getInt(Constants.LAST_FPS, 60)) {
            case 90:
                newFps = 120;
                icon = Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_120);
                break;
            case 120:
                newFps = 60;
                icon = Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_60);
                break;
            default:
                newFps = 90;
                icon = Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_90);
                break;
        }
        sharedPreferences.edit().putInt(Constants.LAST_FPS, newFps).apply();
        SystemProperties.set(Constants.VENDOR_FPS, Integer.toString(newFps));
        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }
}
