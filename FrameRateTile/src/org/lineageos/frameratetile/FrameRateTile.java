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
    private static final String TAG = "FrameRateTile";
    SharedPreferences lastFpsSharedpreferences;

    @Override
    public void onStartListening() {
        super.onStartListening();

        lastFpsSharedpreferences = createDeviceProtectedStorageContext().getSharedPreferences(Constants.FRAMERATETILE, Context.MODE_PRIVATE);
        switch (lastFpsSharedpreferences.getInt(Constants.LASTFPS, 60)) {
            case 60:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_60));
                break;
            case 90:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_90));
                break;
            case 120:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_120));
                break;
        }

        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        int lastFps = lastFpsSharedpreferences.getInt(Constants.LASTFPS, 60);

        SharedPreferences.Editor fpsEditor = lastFpsSharedpreferences.edit();
        switch (lastFps) {
            case 60:
                lastFps = 90;
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_90));
                getQsTile().updateTile();
                fpsEditor.putInt(Constants.LASTFPS, lastFps);
                SystemProperties.set(Constants.VENDOR_FPS, Integer.toString(lastFps));
                break;
            case 90:
                lastFps = 120;
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_120));
                getQsTile().updateTile();
                fpsEditor.putInt(Constants.LASTFPS, lastFps);
                SystemProperties.set(Constants.VENDOR_FPS, Integer.toString(lastFps));
                break;
            case 120:
                lastFps = 60;
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_frame_rate_mode_60));
                getQsTile().updateTile();
                fpsEditor.putInt(Constants.LASTFPS, lastFps);
                SystemProperties.set(Constants.VENDOR_FPS, Integer.toString(lastFps));
                break;
        }
        fpsEditor.commit();
    }
}
