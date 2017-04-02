/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.model.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;

/**
 * @author <a href="mailto:ryo@mm2d.net">大前良介 (OHMAE Ryosuke)</a>
 */
public class RecyclerViewBindingAdapter {
    @BindingAdapter("itemDecoration")
    public static void addItemDecoration(RecyclerView view, ItemDecoration decor) {
        view.addItemDecoration(decor);
    }
}