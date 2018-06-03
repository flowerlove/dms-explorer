/*
 * Copyright (c) 2018 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.settings.theme

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import net.mm2d.android.upnp.cds.MediaServer
import net.mm2d.dmsexplorer.Const

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
internal class ServerColorExtractorDefault : ServerColorExtractor {
    override fun invoke(server: MediaServer, icon: Bitmap?) {
        if (server.getBooleanTag(Const.KEY_HAS_TOOLBAR_COLOR, false)) {
            return
        }
        extract(server, icon)
    }

    override fun invokeAsync(server: MediaServer, icon: Bitmap?) {
        if (server.getBooleanTag(Const.KEY_HAS_TOOLBAR_COLOR, false)) {
            return
        }
        if (icon == null) {
            extractFromPalette(server, null)
        } else {
            Palette.Builder(icon)
                    .generate { palette -> extractFromPalette(server, palette) }
        }
    }

    private fun extract(server: MediaServer, icon: Bitmap?) {
        val palette = icon?.let { Palette.Builder(it).generate() }
        extractFromPalette(server, palette)
    }

    private fun extractFromPalette(server: MediaServer, palette: Palette?) {
        val friendlyName = server.friendlyName
        var expandedColor = GENERATOR.getExpandedToolbarColor(friendlyName)
        var collapsedColor = GENERATOR.getControlColor(friendlyName)
        palette?.apply {
            PaletteUtils.selectLightSwatch(this)?.let {
                expandedColor = it.rgb
            }
            PaletteUtils.selectDarkSwatch(this)?.let {
                collapsedColor = it.rgb
            }
        }
        server.putIntTag(Const.KEY_TOOLBAR_EXPANDED_COLOR, expandedColor)
        server.putIntTag(Const.KEY_TOOLBAR_COLLAPSED_COLOR, collapsedColor)
        server.putBooleanTag(Const.KEY_HAS_TOOLBAR_COLOR, true)
    }

    companion object {
        private val GENERATOR = ThemeColorGeneratorDefault()
    }
}
