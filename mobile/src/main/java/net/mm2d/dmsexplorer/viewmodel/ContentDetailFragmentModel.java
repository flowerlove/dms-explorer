/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import net.mm2d.android.upnp.avt.MediaRenderer;
import net.mm2d.android.upnp.avt.MrControlPoint;
import net.mm2d.android.upnp.avt.MrControlPoint.MrDiscoveryListener;
import net.mm2d.android.upnp.cds.CdsObject;
import net.mm2d.android.util.AribUtils;
import net.mm2d.dmsexplorer.BR;
import net.mm2d.dmsexplorer.R;
import net.mm2d.dmsexplorer.Repository;
import net.mm2d.dmsexplorer.domain.model.MediaServerModel;
import net.mm2d.dmsexplorer.util.ItemSelectUtils;
import net.mm2d.dmsexplorer.util.ThemeUtils;
import net.mm2d.dmsexplorer.view.adapter.ContentPropertyAdapter;

/**
 * @author <a href="mailto:ryo@mm2d.net">大前良介 (OHMAE Ryosuke)</a>
 */
public class ContentDetailFragmentModel extends BaseObservable {
    public final int collapsedColor;
    public final int expandedColor;
    @NonNull
    public final String title;
    @NonNull
    public final ContentPropertyAdapter propertyAdapter;
    public final boolean hasResource;
    public final boolean hasProtectedResource;

    private boolean mCanSend;

    private final Activity mActivity;
    private final MrControlPoint mMrControlPoint;
    private final MrDiscoveryListener mMrDiscoveryListener = new MrDiscoveryListener() {
        @Override
        public void onDiscover(@NonNull final MediaRenderer server) {
            updateCanSend();
        }

        @Override
        public void onLost(@NonNull final MediaRenderer server) {
            updateCanSend();
        }
    };

    public ContentDetailFragmentModel(@NonNull final Activity activity,
                                      @NonNull final Repository repository) {
        final MediaServerModel model = repository.getMediaServerModel();
        if (model == null) {
            throw new IllegalStateException();
        }
        final CdsObject object = model.getSelectedObject();
        if (object == null) {
            throw new IllegalStateException();
        }
        mActivity = activity;
        final String rawTitle = object.getTitle();
        title = AribUtils.toDisplayableString(rawTitle);
        propertyAdapter = new ContentPropertyAdapter(activity, object);
        collapsedColor = ThemeUtils.getAccentColor(rawTitle);
        expandedColor = ThemeUtils.getPastelColor(rawTitle);
        hasResource = object.hasResource();
        hasProtectedResource = object.hasProtectedResource();

        mMrControlPoint = repository.getControlPointModel().getMrControlPoint();
        updateCanSend();
        mMrControlPoint.addMrDiscoveryListener(mMrDiscoveryListener);
    }

    @Bindable
    public boolean getCanSend() {
        return mCanSend;
    }

    private void updateCanSend() {
        mCanSend = mMrControlPoint.getDeviceListSize() > 0 && hasResource;
        notifyPropertyChanged(BR.canSend);
    }

    public void terminate() {
        mMrControlPoint.removeMrDiscoveryListener(mMrDiscoveryListener);
    }

    public void onClickPlay(@NonNull final View view) {
        if (hasProtectedResource) {
            showSnackbar(view);
        } else {
            ItemSelectUtils.play(mActivity, 0);
        }
    }

    public boolean onLongClickPlay(@NonNull final View view) {
        if (hasProtectedResource) {
            showSnackbar(view);
        } else {
            ItemSelectUtils.play(mActivity);
        }
        return true;
    }

    private static void showSnackbar(@NonNull final View view) {
        Snackbar.make(view, R.string.toast_not_support_drm, Snackbar.LENGTH_LONG).show();
    }

    public void onClickSend(@NonNull final View view) {
        ItemSelectUtils.send(mActivity);
    }
}