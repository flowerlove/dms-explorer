package net.mm2d.dmsexplorer.debug

import android.content.Context
import net.mm2d.android.util.RuntimeEnvironment
import net.mm2d.dmsexplorer.BuildConfig
import net.mm2d.log.Logger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

/**
 * @author [大前良介 (OHMAE Ryosuke)](mailto:ryo@mm2d.net)
 */
object DebugData {
    private var pinnedDeviceLocationList = emptyList<String>()

    fun initialize(context: Context) {
        if (!BuildConfig.DEBUG || !RuntimeEnvironment.isEmulator) {
            return
        }
        try {
            val data = context.assets.open("locations.json").readBytes()
            val jsonArray = JSONArray(data.toString(Charset.forName("utf-8")))
            val list = ArrayList<String>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray[i] as JSONObject
                list.add(jsonObject.getString("location"))
            }
            Logger.e { list.toString() }
            pinnedDeviceLocationList = list
        } catch (ignored: IOException) {
        } catch (ignored: JSONException) {
        }
    }

    fun getPinnedDeviceLocationList(): List<String> = pinnedDeviceLocationList
}
