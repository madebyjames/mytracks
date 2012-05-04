/*
 * Copyright 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.android.apps.mytracks.endtoendtest;

import com.google.android.apps.mytracks.TrackListActivity;
import com.google.android.maps.mytracks.R;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Tests the delete of MyTracks.
 * 
 * @author Youtao Liu
 */
public class DeleteTest extends ActivityInstrumentationTestCase2<TrackListActivity> {

  private Instrumentation instrumentation;
  private TrackListActivity activityMyTracks;

  public DeleteTest() {
    super(TrackListActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    instrumentation = getInstrumentation();
    activityMyTracks = getActivity();
    EndToEndTestUtils.setupForAllTest(instrumentation, activityMyTracks);
  }

  /**
   * Deletes all tracks.
   */
  public void testDeleteAllTracks() {
    EndToEndTestUtils.createSimpleTrack(0);
    EndToEndTestUtils.SOLO.goBack();
    instrumentation.waitForIdleSync();
    // There is at least one track.
    ArrayList<ListView> trackListView = EndToEndTestUtils.SOLO.getCurrentListViews();
    assertTrue(trackListView.size() > 0);
    assertTrue(trackListView.get(0).getCount() > 0);

    EndToEndTestUtils.SOLO.sendKey(KeyEvent.KEYCODE_MENU);
    EndToEndTestUtils.findMenuItem(activityMyTracks.getString(R.string.menu_delete_all), true,
        false);
    instrumentation.waitForIdleSync();
    EndToEndTestUtils.rotateAllActivities();
    EndToEndTestUtils.SOLO.clickOnButton(activityMyTracks.getString(R.string.generic_ok));
    instrumentation.waitForIdleSync();
    // There is no track now.
    trackListView = EndToEndTestUtils.SOLO.getCurrentListViews();
    assertEquals(0, trackListView.get(0).getCount());
  }

  /**
   * Deletes only one track.
   */
  public void testDeleteSingleTrack() {
    EndToEndTestUtils.createSimpleTrack(0);
    EndToEndTestUtils.SOLO.goBack();
    // Get the number of track( or tracks)
    ArrayList<ListView> trackListView = EndToEndTestUtils.SOLO.getCurrentListViews();
    int trackNumberOld = trackListView.get(0).getCount();

    EndToEndTestUtils.SOLO.clickLongOnText(EndToEndTestUtils.TRACK_NAME);
    EndToEndTestUtils.findMenuItem(activityMyTracks.getString(R.string.menu_delete), true, true);
    EndToEndTestUtils.SOLO.clickLongOnText(activityMyTracks.getString(R.string.generic_ok));
    instrumentation.waitForIdleSync();
    trackListView = EndToEndTestUtils.SOLO.getCurrentListViews();
    int trackNumberNew = trackListView.get(0).getCount();
    assertEquals(trackNumberOld - 1, trackNumberNew);

  }

  @Override
  protected void tearDown() throws Exception {
    EndToEndTestUtils.SOLO.finishOpenedActivities();
    super.tearDown();
  }

}
